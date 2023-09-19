package com.session.service;

import static com.session.constant.ConstantUsage.ARCHIVE_FLAG_NO;
import static com.session.constant.ConstantUsage.ARCHIVE_FLAG_YES;
import static com.session.constant.ConstantUsage.CUSTOMER_NOT_FOUND;
import static com.session.constant.ConstantUsage.MAX_DAYS;
import static com.session.constant.ConstantUsage.SESSION_CANT_BE_ARCHIVED;
import static com.session.constant.ConstantUsage.SESSION_CANT_BE_DELETED;
import static com.session.constant.ConstantUsage.SESSION_NOT_ACTIVE;
import static com.session.constant.ConstantUsage.SESSION_NOT_FOUND;
import static com.session.constant.ConstantUsage.SESSION_NOT_FOUND_BY_STATUS;
import static com.session.constant.ConstantUsage.STATUS_ACTIVE;
import static com.session.constant.ConstantUsage.STATUS_ARCHIVE;
import static com.session.constant.ConstantUsage.STATUS_DELETE;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.session.dto.EditSessionRequestDto;
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

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SessionServiceImpl implements ISessionService {

	@Autowired
	Mapper mapper;

	@Autowired
	SessionRepository sessionRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	SessionHistRepository sessionHistRepository;

	/**
	 * Creates a new session based on the provided session request data.
	 *
	 * @param sessionRequestDto The DTO containing the session data to be created.
	 * @return A response DTO containing information about the created session.
	 * @throws CustomerNotFoundExcpetion the customer associated with the session is
	 *                                   not found.
	 */

	@Transactional
	@Override
	public SessionResponseDto createSession(SessionRequestDto sessionRequestDto) throws CustomerNotFoundExcpetion {

		Session session = mapper.convertRequestDtoToEntity(sessionRequestDto);

		Optional<Customer> customer = customerRepository.findById(session.getCustomer().getCustomerId());

		if (customer.isPresent()) {

			session.setCustomer(customer.get());

			Session savedSession = sessionRepository.save(session);

			SessionResponseDto sessionResponseDto = mapper.convertEntityToResponseDto(savedSession);
			sessionResponseDto.setArchiveFlag(ARCHIVE_FLAG_NO);
			return sessionResponseDto;

		} else {

			throw new CustomerNotFoundExcpetion(CUSTOMER_NOT_FOUND);
		}

	}

	/**
	 * Archives a session based on the provided session ID, if the conditions for
	 * archiving are met.
	 *
	 * @param sessionId The ID of the session to be archived.
	 * @return A response DTO containing information about the archived session.
	 * @throws SessionCantBeArchivedException If the session cannot be archived due
	 *                                        to time constraints.
	 * @throws SessionNotFoundException       If the specified session ID does not
	 *                                        correspond to an existing session.
	 */

	@Override
	public SessionResponseDto archiveTheSession(String sessionId)
			throws SessionCantBeArchivedException, SessionNotFoundException, SessionNotActiveException {
		Optional<Session> session = sessionRepository.findById(sessionId);

		if (session.isPresent()) {
			Session savedSessionResponse = sessionRepository.findById(sessionId).get();

			if (savedSessionResponse.getStatus() == STATUS_ACTIVE) {

				LocalDateTime date = LocalDateTime.now();

				long count = ChronoUnit.DAYS.between(savedSessionResponse.getUpdatedOn(), date);
				log.info("count" + count);

				if (count > MAX_DAYS) {
					savedSessionResponse.setStatus(STATUS_ARCHIVE);
					Session savedSession = sessionRepository.save(savedSessionResponse);
					SessionResponseDto sessionResponse = mapper.convertEntityToResponseDto(savedSession);
					sessionResponse.setArchiveFlag(ARCHIVE_FLAG_YES);
					return sessionResponse;
				} else {
					throw new SessionCantBeArchivedException(SESSION_CANT_BE_ARCHIVED);
				}
			} else {
				throw new SessionNotActiveException(SESSION_NOT_ACTIVE);

			}
		}

		throw new SessionNotFoundException(SESSION_NOT_FOUND);

	}

	/**
	 * Edits an active session based on the provided session ID and edit session
	 * request data.
	 *
	 * @param sessionId             The ID of the session to be edited.
	 * @param editSessionRequestDto The DTO containing the updated session data.
	 * @return A response DTO containing information about the edited session.
	 * @throws SessionNotFoundException  If the specified session ID does not
	 *                                   correspond to an existing session.
	 * @throws SessionNotActiveException If the session is not in an active state
	 *                                   and cannot be edited.
	 */

	@Override
	public SessionResponseDto editActiveSession(String sessionId, EditSessionRequestDto editSessionRequestDto)
			throws SessionNotFoundException, SessionNotActiveException {
		Optional<Session> session = sessionRepository.findById(sessionId);

		if (session.isPresent()) {

			Session sessionRequest = sessionRepository.findById(sessionId).get();

			if (sessionRequest.getStatus() == STATUS_ACTIVE) {

				LocalDateTime date = LocalDateTime.now();

				sessionRequest.setSessionName(editSessionRequestDto.getSessionName());
				sessionRequest.setRemark(editSessionRequestDto.getRemark());
				sessionRequest.setUpdatedOn(date);
				Session savedSession = sessionRepository.save(sessionRequest);
				SessionResponseDto responseDto = mapper.convertEntityToResponseDto(savedSession);
				responseDto.setArchiveFlag("N");
				return responseDto;
			} else {
				throw new SessionNotActiveException(SESSION_NOT_ACTIVE);
			}

		}

		throw new SessionNotFoundException(SESSION_NOT_FOUND);

	}

	/**
	 * Edits an active session based on the provided session ID and edit session
	 * request data.
	 *
	 * @param sessionId The ID of the session to be edited.
	 * 
	 * @return A response DTO containing information about the edited session.
	 * @throws SessionNotFoundException  If the specified session ID does not
	 *                                   correspond to an existing session.
	 * @throws SessionNotActiveException If the session is not in an active state
	 *                                   and cannot be edited.
	 */

	@Override
	public SessionResponseDto deleteSession(String sessionId) throws SessionNotFoundException, SessionCannotBeDeleted {

		if (!sessionRepository.existsById(sessionId)) {
			throw new SessionNotFoundException(SESSION_NOT_FOUND);
		} else {

			Session session = sessionRepository.findById(sessionId).get();
			if (session.getStatus() == STATUS_ACTIVE) {

				session.setStatus(STATUS_DELETE);
				SessionHist sessionHist = mapper.convertSessionToSessionHist(session);
				SessionHist savedSession = sessionHistRepository.save(sessionHist);
				SessionResponseDto sessionResponseDto = mapper.convertSessionHistToSessionResponseDto(savedSession);

				sessionResponseDto.setUpdatedOn(session.getUpdatedOn());
				sessionResponseDto.setArchiveFlag("N");
				return sessionResponseDto;
			} else {

				throw new SessionCannotBeDeleted(SESSION_CANT_BE_DELETED);

			}

		}

	}

	/**
	 * Retrieves a list of session response DTOs based on the specified status and
	 * 
	 * @param status The status of the sessions to retrieve.
	 * @return A list of session response DTOs with updated archive flags.
	 */

	@Override
	public SessionListResponse getSessions(char status, int pageNumber, int pageSize, String sortBy, String sortDir)
			throws SessionNotFoundByStatus {
		PageRequest pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.fromString(sortDir), sortBy);
		Page<Session> sessionsPage = sessionRepository.findByStatus(status, pageable);

		if (!(status == STATUS_ACTIVE || status == STATUS_ARCHIVE || status == STATUS_DELETE)) {
			throw new SessionNotFoundByStatus(SESSION_NOT_FOUND_BY_STATUS);
		}

		List<Session> sessions = sessionsPage.getContent();
		List<SessionResponseDto> sessionResponseList = new ArrayList<>();
		for (int i = 0; i < sessions.size(); i++) {
			SessionResponseDto sessionResponseDto = convertToResponseDto(sessions.get(i));
			LocalDateTime updatedOn = sessionResponseDto.getUpdatedOn();
			LocalDateTime currentDateTime = LocalDateTime.now();
			long daysDifference = ChronoUnit.DAYS.between(updatedOn, currentDateTime);
			if (STATUS_ACTIVE == status && daysDifference > MAX_DAYS) {
				sessionResponseDto.setArchiveFlag(ARCHIVE_FLAG_YES);

			} else if (STATUS_ARCHIVE == status) {
				sessionResponseDto.setArchiveFlag(ARCHIVE_FLAG_YES);

			} else if (STATUS_ACTIVE == status && STATUS_DELETE == status) {
				sessionResponseDto.setArchiveFlag(ARCHIVE_FLAG_NO);

			}
			sessionResponseList.add(sessionResponseDto);
		}

		return new SessionListResponse(sessionResponseList, sessionsPage.getTotalElements());

	}

	/**
	 * Calculates the archive flag for a given session based on its last update time
	 * and a predefined threshold.
	 *
	 * @param session The session for which the archive flag is calculated.
	 * @return A string representing the archive flag, either
	 *         {@code ARCHIVE_FLAG_NO} or {@code ARCHIVE_FLAG_YES}.
	 * @throws NullPointerException if the input {@code session} is {@code null}.
	 */

	protected String calculateArchiveFlag(@NotNull Session session) {
		LocalDateTime updatedOnPlusDormantDays = session.getUpdatedOn().plusDays(MAX_DAYS);
		return updatedOnPlusDormantDays.isAfter(LocalDateTime.now()) ? ARCHIVE_FLAG_NO : ARCHIVE_FLAG_YES;
	}

	/**
	 * Converts a {@link Session} entity object into a {@link SessionResponseDto}.
	 *
	 * @param session The {@link Session} entity to be converted.
	 * @return A {@link SessionResponseDto} representing the converted session.
	 * @throws SessionNotFoundException If the session is not found or an error
	 *                                  occurs during conversion.
	 */

	protected SessionResponseDto convertToResponseDto(@NotNull Session session) throws SessionNotFoundException {
		SessionResponseDto responseDto = mapper.convertEntityToResponseDto(session);
		String archiveFlag = calculateArchiveFlag(session);
		responseDto.setArchiveFlag(archiveFlag);
		return responseDto;
	}

}
