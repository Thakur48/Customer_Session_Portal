package com.session.controller;

import static com.session.constant.ConstantUsage.SESSION_CANT_BE_DELETED;
import static com.session.constant.ConstantUsage.SESSION_NOT_ACTIVE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.session.dto.CustomerRequestDto;
import com.session.dto.CustomerResponseDto;
import com.session.dto.EditSessionRequestDto;
import com.session.dto.SessionListResponse;
import com.session.dto.SessionRequestDto;
import com.session.dto.SessionResponseDto;
import com.session.exception.CustomerNotFoundExcpetion;
import com.session.exception.SessionCannotBeDeleted;
import com.session.exception.SessionCantBeArchivedException;
import com.session.exception.SessionNotActiveException;
import com.session.exception.SessionNotFoundException;
import com.session.mapper.Mapper;
import com.session.model.Customer;
import com.session.repository.CustomerRepository;
import com.session.service.CustomerServiceImpl;
import com.session.service.SessionServiceImpl;;

class SessionControllerTest {

	@Mock
	SessionServiceImpl sessionService;

	@InjectMocks
	SessionController sessionController;

	@Mock
	EditSessionRequestDto editSessionRequestDto;

	@Mock
	CustomerRequestDto customerRequestDto;

	@Mock
	CustomerResponseDto customerResponseDto;

	@Mock
	CustomerRepository customerRepository;

	@Mock
	Mapper mapper;

	@Mock
	CustomerServiceImpl customerService;

	@Mock
	SessionListResponse sessionListResponse;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		MockMvcBuilders.standaloneSetup(sessionController).build();
	}

	/**
	 * Test case to verify successful creation of a session.
	 *
	 * @throws CustomerNotFoundExcpetion If the customer is not found.
	 */

	@Test
	void testCreateSession_Success() throws CustomerNotFoundExcpetion {

		SessionRequestDto sessionRequestDto = new SessionRequestDto("Thakur", "Thakur Session", "Ramesh", "1");

		LocalDateTime date = LocalDateTime.now();

		Customer customer = new Customer("1", "Thakur");

		SessionResponseDto mockResponseDto = new SessionResponseDto("1", "Thakur", "Thakur Session", "Ramesh", date,
				date, 'A', customer.getCustomerId(), customer.getCustomerName(), "N");
		when(sessionService.createSession(eq(sessionRequestDto))).thenReturn(mockResponseDto);

		ResponseEntity<SessionResponseDto> responseEntity = sessionController.createSession(sessionRequestDto);

		assertNotNull(responseEntity);
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertEquals(mockResponseDto, responseEntity.getBody());
	}

	/**
	 * Test case to verify handling of customer not found during session creation.
	 *
	 * @throws CustomerNotFoundExcpetion If the customer is not found.
	 */

	@Test
	void testCreateSession_CustomerNotFound() throws CustomerNotFoundExcpetion {

		SessionRequestDto requestDto = new SessionRequestDto("Thakur", "Thakur Session", "Ramesh", "");

		when(sessionService.createSession(requestDto)).thenThrow(CustomerNotFoundExcpetion.class);

		assertThrows(CustomerNotFoundExcpetion.class, () -> sessionController.createSession(requestDto));

		// assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,
		// responseEntity.getStatusCode());
	}

	/**
	 * Test case to verify successful archiving of a session.
	 *
	 * @throws SessionNotFoundException       If the session is not found.
	 * @throws SessionCantBeArchivedException If the session can't be archived.
	 */

	@Test
	void testArchiveSession_Successful() throws SessionNotFoundException, SessionCantBeArchivedException {

		String sessionId = "sampleSessionId";
		LocalDateTime date = LocalDateTime.now();

		Customer customer = new Customer("1", "Thakur");
		SessionResponseDto mockResponseDto = new SessionResponseDto("1", "Thakur", "Thakur Session", "Ramesh", date,
				date, 'A', customer.getCustomerId(), customer.getCustomerName(), "N");
		when(sessionService.archiveTheSession(sessionId)).thenReturn(mockResponseDto);

	}

	/**
	 * Test case to verify handling of session not found during archiving.
	 *
	 * @throws SessionNotFoundException       If the session is not found.
	 * @throws SessionCantBeArchivedException If the session can't be archived.
	 */

	@Test
	void testArchiveSession_SessionNotFoundException() throws SessionNotFoundException, SessionCantBeArchivedException {
		String sessionId = "nonExistentSessionId";
		when(sessionService.archiveTheSession(sessionId)).thenThrow(new SessionNotFoundException("Session Not Found"));

		assertThrows(SessionNotFoundException.class, () -> sessionController.archiveSession(sessionId));
		verify(sessionService, times(1)).archiveTheSession(sessionId);
	}

	/**
	 * Test case to verify handling of inactive session during archiving.
	 *
	 * @throws SessionNotFoundException If the session is not found.
	 * @throws SessionCantBeArchivedException If the session can't be archived.
	 * @throws SessionNotActiveException If the session is not active.
	 */
	@Test
     void testArchiveSessionNotActive() throws SessionNotFoundException, SessionCantBeArchivedException, SessionNotActiveException {
        when(sessionService.archiveTheSession(anyString())).thenThrow(new SessionNotActiveException(SESSION_NOT_ACTIVE));

        assertThrows(SessionNotActiveException.class, () -> sessionController.archiveSession("sampleSessionId"));
    }

	/**
	 * Test case to verify handling of session can't be archived.
	 *
	 * @throws SessionNotFoundException       If the session is not found.
	 * @throws SessionCantBeArchivedException If the session can't be archived.
	 */

	@Test
	void testArchiveSession_SessionCantBeArchivedException()
			throws SessionNotFoundException, SessionCantBeArchivedException {
		String sessionId = "problematicSessionId";
		when(sessionService.archiveTheSession(sessionId))
				.thenThrow(new SessionCantBeArchivedException("Session can't be archived"));

		assertThrows(SessionCantBeArchivedException.class, () -> sessionController.archiveSession(sessionId));
		verify(sessionService, times(1)).archiveTheSession(sessionId);
	}

	/**
	 * Test case to verify successful editing of an active session.
	 *
	 * @throws SessionNotFoundException  If the session is not found.
	 * @throws SessionNotActiveException If the session is not active.
	 */

	@Test
	void testEditActiveSession_Success() throws SessionNotFoundException, SessionNotActiveException {

		String sessionId = "sampleSessionId";
		EditSessionRequestDto editSessionRequestDto = new EditSessionRequestDto("Thakur Session",
				"Thakur First Session");

		LocalDateTime date = LocalDateTime.now();

		Customer customer = new Customer("1", "Thakur");
		SessionResponseDto mockResponseDto = new SessionResponseDto("1", "Thakur", "Thakur Session", "Ramesh", date,
				date, 'A', customer.getCustomerId(), customer.getCustomerName(), "N");

		when(sessionService.editActiveSession(sessionId, editSessionRequestDto)).thenReturn(mockResponseDto);

		ResponseEntity<SessionResponseDto> responseEntity = sessionController.editActiveSession(sessionId,
				editSessionRequestDto);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(mockResponseDto, responseEntity.getBody());
		verify(sessionService, times(1)).editActiveSession(sessionId, editSessionRequestDto);
	}

	/**
	 * Test case to verify handling of session not found during editing.
	 *
	 * @throws SessionNotFoundException  If the session is not found.
	 * @throws SessionNotActiveException If the session is not active.
	 */

	@Test
	void testEditActiveSession_SessionNotFoundException() throws SessionNotFoundException, SessionNotActiveException {
		String sessionId = "nonExistentSessionId";
		EditSessionRequestDto editSessionRequestDto = new EditSessionRequestDto("Thakur Session",
				"Thakur First Session");
		when(sessionService.editActiveSession(sessionId, editSessionRequestDto))
				.thenThrow(new SessionNotFoundException("Serssion Not Found"));

		assertThrows(SessionNotFoundException.class,
				() -> sessionController.editActiveSession(sessionId, editSessionRequestDto));
		verify(sessionService, times(1)).editActiveSession(sessionId, editSessionRequestDto);
	}

	/**
	 * Test case to verify handling of inactive session during editing.
	 *
	 * @throws SessionNotFoundException  If the session is not found.
	 * @throws SessionNotActiveException If the session is not active.
	 */

	@Test
	void testEditActiveSession_SessionNotActiveException() throws SessionNotFoundException, SessionNotActiveException {
		String sessionId = "inactiveSessionId";
		EditSessionRequestDto editSessionRequestDto = new EditSessionRequestDto("Thakur Session",
				"Thakur First Session");
		when(sessionService.editActiveSession(sessionId, editSessionRequestDto))
				.thenThrow(new SessionNotActiveException("Session Not Active"));

		assertThrows(SessionNotActiveException.class,
				() -> sessionController.editActiveSession(sessionId, editSessionRequestDto));
		verify(sessionService, times(1)).editActiveSession(sessionId, editSessionRequestDto);
	}

	/**
	 * Test case to verify successful deletion of a session.
	 *
	 * @throws SessionNotFoundException If the session is not found.
	 */

	@Test
	void testDeleteSession_Success() throws SessionNotFoundException {
		String sessionId = "sampleSessionId";
		SessionResponseDto sessionResponseDto = new SessionResponseDto();
		when(sessionService.deleteSession(sessionId)).thenReturn(sessionResponseDto);

		ResponseEntity<SessionResponseDto> responseEntity = sessionController.deleteSession(sessionId);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(sessionResponseDto, responseEntity.getBody());
		verify(sessionService, times(1)).deleteSession(sessionId);
	}

	/**
	 * Test case to verify handling of session not found during deletion.
	 *
	 * @throws SessionNotFoundException If the session is not found.
	 */

	@Test
	void testDeleteSession_SessionNotFoundException() throws SessionNotFoundException {

		String sessionId = "nonExistentSessionId";
		when(sessionService.deleteSession(sessionId)).thenThrow(new SessionNotFoundException("Session Not Found"));

		assertThrows(SessionNotFoundException.class, () -> sessionController.deleteSession(sessionId));
		verify(sessionService, times(1)).deleteSession(sessionId);
	}

	/**
	 * Test case to verify handling of session that cannot be deleted.
	 *
	 * @throws SessionNotFoundException If the session is not found.
	 * @throws SessionCannotBeDeleted If the session cannot be deleted.
	 */

	@Test
     void testDeleteSessionCannotBeDeleted() throws SessionNotFoundException, SessionCannotBeDeleted {
        when(sessionService.deleteSession(anyString())).thenThrow(new SessionCannotBeDeleted(SESSION_CANT_BE_DELETED));

        
		assertThrows(SessionCannotBeDeleted.class, () -> sessionController.deleteSession("sampleSessionId"));

    }

	/**
	 * Test case to verify successful retrieval of sessions by a valid status.
	 *
	 * @throws SessionNotFoundException If no sessions are found for the given
	 *                                  status.
	 */

	@Test
	void testGetSessionsByStatus_ValidStatus() throws SessionNotFoundException {
		char validStatus = 'A';
		int pageNumber = 1;
		int pageSize = 10;
		String sortBy = "sessionName";
		String sortDir = "asc";

		SessionListResponse mockResponse = new SessionListResponse(); // Create a mock response
		when(sessionService.getSessions(validStatus, pageNumber, pageSize, sortBy, sortDir)).thenReturn(mockResponse);

		ResponseEntity<SessionListResponse> responseEntity = sessionController.getSessionsByStatus(validStatus,
				pageNumber, pageSize, sortBy, sortDir);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
	}

	/**
	 * Test case to verify handling of session not found when retrieving sessions by
	 * status.
	 *
	 * @throws SessionNotFoundException If no sessions are found for the given
	 *                                  status.
	 */

	@Test
	void testGetSessionsByStatus_SessionNotFound() throws SessionNotFoundException {
		char invalidStatus = 'U';
		int pageNumber = 1;
		int pageSize = 10;
		String sortBy = "sessionName";
		String sortDir = "asc";

		when(sessionController.getSessionsByStatus(invalidStatus, pageNumber, pageSize, sortBy, sortDir))
				.thenThrow(new SessionNotFoundException("Session Not Found"));

	}

}
