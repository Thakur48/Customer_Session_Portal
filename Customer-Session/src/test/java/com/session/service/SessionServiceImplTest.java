package com.session.service;

import static com.session.constant.ConstantUsage.ARCHIVE_FLAG_NO;
import static com.session.constant.ConstantUsage.ARCHIVE_FLAG_YES;
import static com.session.constant.ConstantUsage.MAX_DAYS;
import static com.session.constant.ConstantUsage.SESSION_NOT_FOUND;
import static com.session.constant.ConstantUsage.SESSION_NOT_FOUND_BY_STATUS;
import static com.session.constant.ConstantUsage.STATUS_ACTIVE;
import static com.session.constant.ConstantUsage.STATUS_ARCHIVE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.session.dto.CustomerRequestDto;
import com.session.dto.EditSessionRequestDto;
import com.session.dto.SessionCustomerDto;
import com.session.dto.SessionListResponse;
import com.session.dto.SessionRequestDto;
import com.session.dto.SessionResponseDto;
import com.session.exception.CustomerNotFoundExcpetion;
import com.session.exception.SessionCannotBeDeleted;
import com.session.exception.SessionCantBeArchivedException;
import com.session.exception.SessionNotActiveException;
import com.session.exception.SessionNotFoundByStatus;
import com.session.exception.SessionNotFoundException;
import com.session.mapper.Mapper;
import com.session.model.Customer;
import com.session.model.Session;
import com.session.model.SessionHist;
import com.session.repository.CustomerRepository;
import com.session.repository.SessionHistRepository;
import com.session.repository.SessionRepository;

@ExtendWith(MockitoExtension.class)

class SessionServiceImplTest {

	@Mock
	SessionRequestDto sessionRequestDto;

	@Mock
	SessionCustomerDto sessionCustomerDto;

	@Mock
	SessionRepository sessionRepository;

	@Mock
	SessionResponseDto sessionResponseDto;

	@InjectMocks
	SessionServiceImpl sessionService;

	@Mock
	CustomerRepository customerRepository;

	@Mock
	CustomerRequestDto customerRequestDto;

	@Mock
	Customer customer;

	@Mock
	Mapper mapper;

	@Mock
	Session sessions;

	@Mock
	SessionHist sessionHist;

	@Mock
	SessionHistRepository sessionHistRepository;

	@Mock
	CustomerServiceImpl customerService;

	@Mock
	SessionListResponse sessionListResponse;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	/**
	 * Test case to verify the successful creation of a session.
	 *
	 * @throws CustomerNotFoundException If the customer is not found.
	 */

	@Test
	void testCreateSession_Successful() throws CustomerNotFoundExcpetion {

		SessionRequestDto requestDto = new SessionRequestDto("Thakur", "Thakur Session", "Ramesh", "1");

		LocalDateTime date = LocalDateTime.now();
		Customer customer = new Customer("1", "Thakur");
		Session session = new Session("1", "Thakur", "Thakur Session", "Ramesh", date, date, 'A', customer);

		when(mapper.convertRequestDtoToEntity(requestDto)).thenReturn(session);
		when(customerRepository.findById(any())).thenReturn(Optional.of(customer));
		when(sessionRepository.save(any(Session.class))).thenReturn(session);
		when(mapper.convertEntityToResponseDto(session)).thenReturn(new SessionResponseDto());

		SessionResponseDto responseDto = sessionService.createSession(requestDto);

		assertNotNull(responseDto);

		verify(sessionRepository).save(session);
	}

	/**
	 * Test case to verify that a {@link CustomerNotFoundException} is thrown when
	 * attempting to create a session with a non-existing customer.
	 */
	@Test
	void testCreateSession_CustomerNotFound() {

		SessionRequestDto requestDto = new SessionRequestDto("Thakur", "Thakur Session", "Ramesh", "1");

		LocalDateTime date = LocalDateTime.now();
		Customer customer = new Customer("1", "Thakur");
		Session session = new Session("1", "Thakur", "Thakur Session", "Ramesh", date, date, 'A', customer);
		when(mapper.convertRequestDtoToEntity(requestDto)).thenReturn(session);
		when(customerRepository.findById(any())).thenReturn(Optional.empty());

		assertThrows(CustomerNotFoundExcpetion.class, () -> sessionService.createSession(requestDto));
		verify(sessionRepository, never()).save(session);
	}

	/**
	 * Test case to verify that a {@link SessionCantBeArchivedException} is thrown
	 * when attempting to archive a session that cannot be archived due to time
	 * constraints.
	 */

	@Test
	void testArchiveTheSession_SessionCantBeArchivedException() {

		LocalDateTime date = LocalDateTime.now();
		LocalDateTime updatedOn = LocalDateTime.now().minusDays(5);

		Session session = new Session("1", "Thakur", "Thakur Session", "Ramesh", date, updatedOn, 'A', customer);

		when(sessionRepository.findById(any())).thenReturn(Optional.of(session));

		assertThrows(SessionCantBeArchivedException.class, () -> sessionService.archiveTheSession("1"));
	}

	/**
	 * Test case to verify that a {@link SessionNotFoundException} is thrown
	 * when attempting to archive a session that doesn't exist.
	 */
	@Test
	    void testArchiveTheSession_SessionNotFoundException() {
	       
	        when(sessionRepository.findById(any())).thenReturn(Optional.empty());

	        assertThrows(SessionNotFoundException.class, () -> sessionService.archiveTheSession("1"));
	    }

	/**
	 * Test case to verify that a {@link SessionNotActiveException} is thrown when
	 * attempting to archive a session that is not in an active state.
	 */
	@Test
	void testArchiveSessionNotActive()
			throws SessionCantBeArchivedException, SessionNotFoundException, SessionNotActiveException {
		LocalDateTime date = LocalDateTime.now();

		Session inactiveSession = new Session("1", "Thakur", "Thakur Session", "Ramesh", date, date, 'A', customer);
		inactiveSession.setStatus(STATUS_ARCHIVE);

		when(sessionRepository.findById(any())).thenReturn(Optional.of(inactiveSession));

		assertThrows(SessionNotActiveException.class, () -> sessionService.archiveTheSession("1"));
	}

	/**
	 * Test case to verify the successful editing of an active session.
	 *
	 * @throws SessionNotFoundException  If the session is not found.
	 * @throws SessionNotActiveException If the session is not in an active state.
	 */

	@Test
	void testEditActiveSessionSuccess() throws SessionNotFoundException, SessionNotActiveException {
		EditSessionRequestDto editRequest = new EditSessionRequestDto();
		editRequest.setSessionName("New Session Name");
		editRequest.setRemark("Updated Remark");

		Session activeSession = new Session();
		activeSession.setStatus(STATUS_ACTIVE);

		when(sessionRepository.findById(any())).thenReturn(Optional.of(activeSession));
		when(sessionRepository.save(any(Session.class))).thenReturn(activeSession);

		when(mapper.convertEntityToResponseDto(activeSession)).thenReturn(new SessionResponseDto());

		SessionResponseDto responseDto = sessionService.editActiveSession("sampleSessionId", editRequest);

		assertNotNull(responseDto);
		assertEquals("N", responseDto.getArchiveFlag());
	}

	/**
	 * Test case to verify that a {@link SessionNotActiveException} is thrown when
	 * attempting to edit a session that is not in an active state.
	 *
	 * @throws SessionNotFoundException  If the session is not found.
	 * @throws SessionNotActiveException If the session is not in an active state.
	 */

	@Test
	void testEditActiveSessionNotActive() throws SessionNotFoundException, SessionNotActiveException {
		EditSessionRequestDto editRequest = new EditSessionRequestDto();
		editRequest.setSessionName("New Session Name");
		editRequest.setRemark("Updated Remark");

		Session inactiveSession = new Session();
		inactiveSession.setStatus(STATUS_ARCHIVE);

		when(sessionRepository.findById(any())).thenReturn(Optional.of(inactiveSession));

		assertThrows(SessionNotActiveException.class,
				() -> sessionService.editActiveSession("sampleSessionId", editRequest));
	}

	/**
	 * Test case to verify that a {@link SessionNotFoundException} is thrown when
	 * attempting to edit a session that doesn't exist.
	 */

	@Test
	void testEditActiveSession_SessionNotFound() {

		EditSessionRequestDto requestDto = new EditSessionRequestDto("Thakur", "XYZ");
		when(sessionRepository.findById(any())).thenReturn(Optional.empty());

		assertThrows(SessionNotFoundException.class, () -> sessionService.editActiveSession("1", requestDto));
		verify(sessionRepository, never()).save(any(Session.class));
	}

	/**
	 * Test case to verify the successful deletion of a session.
	 *
	 * @throws SessionNotFoundException If the session is not found.
	 */

	@Test
	void testDeleteSession_Successful() throws SessionNotFoundException {

		LocalDateTime date = LocalDateTime.now();
		Customer customer = new Customer("1", "Thakur");
		Session session = new Session("1", "Thakur", "Thakur Session", "Ramesh", date, date, 'A', customer);

		when(sessionRepository.existsById(any())).thenReturn(true);
		when(sessionRepository.findById(any())).thenReturn(Optional.of(session));
		when(mapper.convertSessionToSessionHist(session)).thenReturn(new SessionHist());
		when(sessionHistRepository.save(any(SessionHist.class))).thenReturn(new SessionHist());
		when(mapper.convertSessionHistToSessionResponseDto(any(SessionHist.class)))
				.thenReturn(new SessionResponseDto());

		SessionResponseDto responseDto = sessionService.deleteSession("1");

		assertNotNull(responseDto);
		assertEquals('D', session.getStatus());
	}

	/**
	 * Test case to verify that a {@link SessionNotFoundException} is thrown
	 * when attempting to delete a session that doesn't exist.
	 */
	@Test
    void testDeleteSession_SessionNotFound() {
        
        when(sessionRepository.existsById(any())).thenReturn(false);

        assertThrows(SessionNotFoundException.class, () -> sessionService.deleteSession("1"));
    }

	/**
	 * Test case to verify that a {@link SessionCannotBeDeleted} is thrown when
	 * attempting to delete a session that cannot be deleted due to its status.
	 */
	@Test
	void testDeleteSession_SessionCannotBeDeleted() {

		LocalDateTime date = LocalDateTime.now();
		Customer customer = new Customer("1", "Thakur");
		Session session = new Session("1", "Thakur", "Thakur Session", "Ramesh", date, date, 'A', customer);

		session.setStatus('X');

		when(sessionRepository.existsById(any())).thenReturn(true);
		when(sessionRepository.findById(any())).thenReturn(java.util.Optional.of(session));

		assertThrows(SessionCannotBeDeleted.class, () -> sessionService.deleteSession("1"));
	}

	/**
	 * Test case to verify that a {@link SessionNotFoundByStatus} is thrown when
	 * attempting to retrieve sessions with an invalid status.
	 *
	 * @throws SessionNotFoundByStatus If sessions with the specified status are not
	 *                                 found.
	 */

	@Test
	void testGetSessions_Negative_InvalidStatus() throws SessionNotFoundByStatus {
		char sessionStatus = 'B';
		int pageNumber = 0;
		int pageSize = 5;
		String sortBy = "updatedOn";
		String sortDir = "desc";
		assertThrows(SessionNotFoundByStatus.class,
				() -> sessionService.getSessions(sessionStatus, pageNumber, pageSize, sortBy, sortDir));
	}

	/**
	 * Test case to verify that a {@link SessionNotFoundByStatus} is thrown when
	 * attempting to retrieve sessions with a specific status, but the resulting
	 * page is empty.
	 *
	 * @throws SessionNotFoundByStatus If sessions with the specified status are not
	 *                                 found.
	 */

	@Test
	void testGetSessions_Negative_EmptyPage() throws SessionNotFoundByStatus {
		char sessionStatus = 'Y';
		int pageNumber = 0;
		int pageSize = 5;
		String sortBy = "updatedOn";
		String sortDir = "desc";
		Page<Session> mockPage = mock(Page.class);

		when(sessionRepository.findByStatus(ArgumentMatchers.eq(sessionStatus), any(PageRequest.class)))
				.thenReturn(mockPage);
		assertThrows(SessionNotFoundByStatus.class,
				() -> sessionService.getSessions(sessionStatus, pageNumber, pageSize, sortBy, sortDir));
	}

	/**
	 * Test case to verify the calculation of the archive flag when the session's
	 * updatedOn timestamp is older than a certain threshold.
	 *
	 * @throws SessionNotFoundException If the session is not found.
	 */
	@Test
	void testCalculateArchiveFlag_Positive() throws SessionNotFoundException {
		Session session = new Session();
		LocalDateTime updatedOn = LocalDateTime.now().minusDays(10);
		session.setUpdatedOn(updatedOn);
		String archiveFlag = sessionService.calculateArchiveFlag(session);

		assertEquals(ARCHIVE_FLAG_YES, archiveFlag);
	}

	/**
	 * Test case to verify the calculation of the archive flag when the session's
	 * updatedOn timestamp is not older than a certain threshold, resulting in no
	 * archive flag being set.
	 *
	 * @throws SessionNotFoundException If the session is not found.
	 */

	@Test
	void testCalculateArchiveFlag_Negative() throws SessionNotFoundException {
		Session session = new Session();
		LocalDateTime updatedOn = LocalDateTime.now().plusDays(10);
		session.setUpdatedOn(updatedOn);
		try {
			String archiveFlag = sessionService.calculateArchiveFlag(session);

			assertEquals(ARCHIVE_FLAG_NO, archiveFlag);
		} catch (Exception e) {
			throw new SessionNotFoundException(SESSION_NOT_FOUND + session.getSessionId());
		}
	}

	/**
	 * Test case to verify the calculation of the archive flag when the session's
	 * updatedOn timestamp is not older than a certain threshold, resulting in the
	 * absence of an archive flag.
	 *
	 * @throws SessionNotFoundException If the session is not found.
	 */

	@Test
	void testConvertToResponseDto_Positive() throws SessionNotFoundException {
		Session session = new Session();
		LocalDateTime updatedOn = LocalDateTime.now().minusDays(10);
		session.setUpdatedOn(updatedOn);
		when(mapper.convertEntityToResponseDto(session)).thenReturn(new SessionResponseDto());

		SessionResponseDto responseDto = sessionService.convertToResponseDto(session);
		assertNotNull(responseDto);
		assertEquals(ARCHIVE_FLAG_YES, responseDto.getArchiveFlag());
	}

	/**
	 * Test case to verify the conversion of a session to a response DTO when the
	 * session's updatedOn timestamp is not older than a certain threshold,
	 * resulting in the absence of an archive flag.
	 *
	 * @throws SessionNotFoundException If the session is not found by status.
	 */

	@Test
	void testConvertToResponseDto_Negative() throws SessionNotFoundException {
		Session session = new Session();
		LocalDateTime updatedOn = LocalDateTime.now().plusDays(10);
		session.setUpdatedOn(updatedOn);
		try {
			when(mapper.convertEntityToResponseDto(session)).thenReturn(new SessionResponseDto());

			SessionResponseDto responseDto = sessionService.convertToResponseDto(session);

			assertNotNull(responseDto);
			assertEquals(ARCHIVE_FLAG_NO, responseDto.getArchiveFlag());
		} catch (Exception e) {
			throw new SessionNotFoundException(SESSION_NOT_FOUND_BY_STATUS + session);
		}
	}

	/**
	 * Test case to verify successful archiving of a session.
	 *
	 * @throws SessionCantBeArchivedException If the session cannot be archived.
	 * @throws SessionNotFoundException       If the session is not found.
	 * @throws SessionNotActiveException      If the session is not active.
	 */

	@Test
	void testArchiveSessionSuccess()
			throws SessionCantBeArchivedException, SessionNotFoundException, SessionNotActiveException {

		LocalDateTime updatedOn = LocalDateTime.now().minusDays(MAX_DAYS + 1);
		LocalDateTime createdDate = LocalDateTime.now().minusDays(MAX_DAYS + 30);

		Customer customer = new Customer("2", "Thakur");
		Session activeSession = new Session("1", "Thakur", "Thakur Session", "Ramesh", createdDate, updatedOn, 'A',
				customer);

		when(sessionRepository.findById(any())).thenReturn(Optional.of(activeSession));
		when(sessionRepository.save(activeSession)).thenReturn(activeSession);

		when(mapper.convertEntityToResponseDto(activeSession)).thenReturn(new SessionResponseDto("1", "Thakur",
				"Thakur Session", "Ramesh", createdDate, activeSession.getUpdatedOn(), 'X', "2", "Thakur", "Y"));

		SessionResponseDto responseDto = sessionService.archiveTheSession("1");

		assertNotNull(responseDto);
		assertEquals(ARCHIVE_FLAG_YES, responseDto.getArchiveFlag());
		assertEquals(STATUS_ARCHIVE, activeSession.getStatus());
	}

	/**
	 * Test case to verify the behavior of 'getSessions' when retrieving sessions
	 * with a valid status.
	 */

	@Test
	void testGetSessions_Positive() throws SessionNotFoundException {
		char sessionStatus = STATUS_ACTIVE;
		int pageNumber = 0;
		int pageSize = 5;
		String sortBy = "updatedOn";
		String sortDir = "desc";
		List<Session> mockSessionList = new ArrayList<>();
		LocalDateTime now = LocalDateTime.now();
		for (int i = 0; i < 5; i++) {
			Session session = new Session();
			session.setSessionId("123" + i);
			session.setSessionName("Loan" + i);
			session.setRemark("ok" + i);
			session.setUpdatedOn(now.minusDays(i));
			session.setCreatedBy("Rohith" + i);
			session.setStatus(sessionStatus);
			mockSessionList.add(session);
		}
		Page<Session> mockPage = new PageImpl<>(mockSessionList);
		when(sessionRepository.findByStatus(eq(sessionStatus), any(PageRequest.class))).thenReturn(mockPage);
		when(mapper.convertEntityToResponseDto(any(Session.class))).thenAnswer(invocation -> {
			Session session = invocation.getArgument(0);
			SessionResponseDto sessionResponseDto = new SessionResponseDto();
			sessionResponseDto.setUpdatedOn(session.getUpdatedOn());
			return sessionResponseDto;
		});

		SessionListResponse response = sessionService.getSessions(sessionStatus, pageNumber, pageSize, sortBy, sortDir);

		assertNotNull(response);
		assertEquals(5, response.getContent().size());
		assertEquals(5, response.getTotalElements());

		response.getContent().forEach(sessionResponseDto -> {
			if (STATUS_ACTIVE == sessionStatus) {
				long daysDifference = ChronoUnit.DAYS.between(sessionResponseDto.getUpdatedOn(), LocalDateTime.now());
				if (daysDifference > MAX_DAYS) {
					assertEquals(ARCHIVE_FLAG_YES, sessionResponseDto.getArchiveFlag());
				} else {
					assertEquals(ARCHIVE_FLAG_NO, sessionResponseDto.getArchiveFlag());
				}
			} else if (STATUS_ARCHIVE == sessionStatus) {
				assertEquals(ARCHIVE_FLAG_YES, sessionResponseDto.getArchiveFlag());
			} else {
				assertEquals(ARCHIVE_FLAG_NO, sessionResponseDto.getArchiveFlag());
			}
		});
	}

	/**
	 * Test case to verify 'getSessions' when all sessions have STATUS_ACTIVE and
	 * the archive flag should be 'ARCHIVE_FLAG_YES'.
	 */

	@Test
	void testGetSessions_Positive_ActiveStatusWithArchiveFlagYes() throws SessionNotFoundException {
		char sessionStatus = STATUS_ACTIVE;
		int pageNumber = 0;
		int pageSize = 5;
		String sortBy = "updatedOn";
		String sortDir = "desc";
		List<Session> mockSessionList = new ArrayList<>();
		LocalDateTime now = LocalDateTime.now();
		for (int i = 0; i < 5; i++) {
			Session session = new Session();
			session.setSessionId("123" + i);
			session.setSessionName("Loan" + i);
			session.setRemark("ok" + i);
			session.setUpdatedOn(now.minusDays(MAX_DAYS + 1));
			session.setCreatedBy("Rohith" + i);
			session.setStatus(sessionStatus);
			mockSessionList.add(session);
		}
		Page<Session> mockPage = new PageImpl<>(mockSessionList);
		when(sessionRepository.findByStatus(eq(sessionStatus), any(PageRequest.class))).thenReturn(mockPage);
		when(mapper.convertEntityToResponseDto(any(Session.class))).thenAnswer(invocation -> {
			Session session = invocation.getArgument(0);
			SessionResponseDto sessionResponseDto = new SessionResponseDto();
			sessionResponseDto.setUpdatedOn(session.getUpdatedOn());
			return sessionResponseDto;
		});

		SessionListResponse response = sessionService.getSessions(sessionStatus, pageNumber, pageSize, sortBy, sortDir);

		assertNotNull(response);
		assertEquals(5, response.getContent().size());
		assertEquals(5, response.getTotalElements());

		response.getContent().forEach(sessionResponseDto -> {
			long daysDifference = ChronoUnit.DAYS.between(sessionResponseDto.getUpdatedOn(), LocalDateTime.now());
			if (daysDifference > MAX_DAYS) {
				assertEquals(ARCHIVE_FLAG_YES, sessionResponseDto.getArchiveFlag());
			} else {
				assertEquals(ARCHIVE_FLAG_NO, sessionResponseDto.getArchiveFlag());
			}
		});
	}

	/**
	 * Test case to verify 'getSessions' when all sessions have STATUS_ARCHIVE and
	 * the archive flag should be 'ARCHIVE_FLAG_YES'.
	 */

	@Test
	void testGetSessions_Positive_ArchiveStatusWithArchiveFlagYes() throws SessionNotFoundException {
		char sessionStatus = STATUS_ARCHIVE;
		int pageNumber = 0;
		int pageSize = 5;
		String sortBy = "updatedOn";
		String sortDir = "desc";
		List<Session> mockSessionList = new ArrayList<>();
		LocalDateTime now = LocalDateTime.now();
		for (int i = 0; i < 5; i++) {
			Session session = new Session();
			session.setSessionId("123" + i);
			session.setSessionName("Loan" + i);
			session.setRemark("ok" + i);
			session.setUpdatedOn(now.minusDays(i));
			session.setCreatedBy("Rohith" + i);
			session.setStatus(sessionStatus);
			mockSessionList.add(session);
		}
		Page<Session> mockPage = new PageImpl<>(mockSessionList);
		when(sessionRepository.findByStatus(eq(sessionStatus), any(PageRequest.class))).thenReturn(mockPage);
		when(mapper.convertEntityToResponseDto(any(Session.class))).thenAnswer(invocation -> {
			Session session = invocation.getArgument(0);
			SessionResponseDto sessionResponseDto = new SessionResponseDto();
			sessionResponseDto.setUpdatedOn(session.getUpdatedOn());
			return sessionResponseDto;
		});

		SessionListResponse response = sessionService.getSessions(sessionStatus, pageNumber, pageSize, sortBy, sortDir);

		assertNotNull(response);
		assertEquals(5, response.getContent().size());
		assertEquals(5, response.getTotalElements());

		response.getContent().forEach(sessionResponseDto -> {
			assertEquals(ARCHIVE_FLAG_YES, sessionResponseDto.getArchiveFlag());
		});
	}

	/**
	 * Test case to verify 'getSessions' when all sessions have a status other than
	 * STATUS_ACTIVE or STATUS_ARCHIVE, and the archive flag should be
	 * 'ARCHIVE_FLAG_NO'.
	 */
	@Test
	void testGetSessions_Positive_OtherStatusWithArchiveFlagNo() throws SessionNotFoundException {
		char sessionStatus = STATUS_ACTIVE;
		int pageNumber = 0;
		int pageSize = 5;
		String sortBy = "updatedOn";
		String sortDir = "desc";
		List<Session> mockSessionList = new ArrayList<>();
		LocalDateTime now = LocalDateTime.now();
		for (int i = 0; i < 5; i++) {
			Session session = new Session();
			session.setSessionId("123" + i);
			session.setSessionName("Loan" + i);
			session.setRemark("ok" + i);
			session.setUpdatedOn(now.minusDays(i));
			session.setCreatedBy("Rohith" + i);
			session.setStatus(sessionStatus);
			mockSessionList.add(session);
		}
		Page<Session> mockPage = new PageImpl<>(mockSessionList);
		when(sessionRepository.findByStatus(eq(sessionStatus), any(PageRequest.class))).thenReturn(mockPage);
		when(mapper.convertEntityToResponseDto(any(Session.class))).thenAnswer(invocation -> {
			Session session = invocation.getArgument(0);
			SessionResponseDto sessionResponseDto = new SessionResponseDto();
			sessionResponseDto.setUpdatedOn(session.getUpdatedOn());
			return sessionResponseDto;
		});

		SessionListResponse response = sessionService.getSessions(sessionStatus, pageNumber, pageSize, sortBy, sortDir);

		assertNotNull(response);
		assertEquals(5, response.getContent().size());
		assertEquals(5, response.getTotalElements());

		response.getContent().forEach(sessionResponseDto -> {
			assertEquals(ARCHIVE_FLAG_NO, sessionResponseDto.getArchiveFlag());
		});
	}

}
