package com.session.controller;

import static com.session.constant.ConstantUsage.ARCHIVE_MAPPING;
import static com.session.constant.ConstantUsage.PAGE_NUMBER;
import static com.session.constant.ConstantUsage.PAGE_SIZE;
import static com.session.constant.ConstantUsage.SESSION_ID;
import static com.session.constant.ConstantUsage.SORT_BY;
import static com.session.constant.ConstantUsage.SORT_DIR;
import static com.session.constant.ConstantUsage.STATUS_REQ;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.session.constant.ConstantUsage;
import com.session.dto.EditSessionRequestDto;
import com.session.dto.SessionListResponse;
import com.session.dto.SessionRequestDto;
import com.session.dto.SessionResponseDto;
import com.session.exception.CustomerNotFoundExcpetion;
import com.session.exception.SessionCannotBeDeleted;
import com.session.exception.SessionCantBeArchivedException;
import com.session.exception.SessionNameInvalidException;
import com.session.exception.SessionNotActiveException;
import com.session.exception.SessionNotFoundException;
import com.session.repository.SessionRepository;
import com.session.service.ISessionService;

@RestController
@RequestMapping(ConstantUsage.SESSION_MAPPING)
public class SessionController {

	@Autowired
	ISessionService sessionService;

	@Autowired
	SessionRepository sessionRepository;

	/**
	 * Creates a new session based on the provided session request data.
	 *
	 * @param sessionRequest The DTO containing the session data to be created.
	 * @return A response entity containing the created session response DTO.
	 * @throws CustomerNotFoundExcpetion If the customer associated with the session
	 *                                   is not found.
	 */

	@PostMapping()
	public ResponseEntity<SessionResponseDto> createSession(@RequestBody @Valid SessionRequestDto sessionRequest)
			throws CustomerNotFoundExcpetion,SessionNameInvalidException {
		SessionResponseDto sessionResponseDto = sessionService.createSession(sessionRequest);

		return new ResponseEntity<>(sessionResponseDto, HttpStatus.CREATED);

	}

	/**
	 * Archives a session based on the provided session ID.
	 *
	 * @param sessionId The ID of the session to be archived.
	 * @return A response entity containing the archived session response DTO.
	 * @throws SessionNotFoundException       If the specified session ID does not
	 *                                        correspond to an existing session.
	 * @throws SessionCantBeArchivedException If the session cannot be archived due
	 *                                        to time constraints.
	 */

	@PatchMapping(ARCHIVE_MAPPING)
	public ResponseEntity<SessionResponseDto> archiveSession(@PathVariable @Valid String sessionId)
			throws SessionNotFoundException, SessionCantBeArchivedException,SessionNotActiveException {
		SessionResponseDto sessionResponseDto = sessionService.archiveTheSession(sessionId);

		return new ResponseEntity<>(sessionResponseDto, HttpStatus.OK);

	}

	/**
	 * Edits an active session based on the provided session ID and edit session
	 * request data.
	 *
	 * @param sessionId             The ID of the session to be edited.
	 * @param editSessionRequestDto The DTO containing the updated session data.
	 * @return A response entity containing the edited session response DTO.
	 * @throws SessionNotFoundException  If the specified session ID does not
	 *                                   correspond to an existing session.
	 * @throws SessionNotActiveException If the session is not in an active state
	 *                                   and cannot be edited.
	 */

	@PutMapping(SESSION_ID)
	public ResponseEntity<SessionResponseDto> editActiveSession(@PathVariable String sessionId,
			@RequestBody @Valid EditSessionRequestDto editSessionRequestDto)
			throws SessionNotFoundException, SessionNotActiveException {
		SessionResponseDto sessionResponseDto = sessionService.editActiveSession(sessionId, editSessionRequestDto);

		return new ResponseEntity<>(sessionResponseDto, HttpStatus.OK);

	}

	/**
	 * Deletes a session based on the provided session ID.
	 *
	 * @param sessionId The ID of the session to be deleted.
	 * @return A response entity containing the deleted session response DTO.
	 * @throws SessionNotFoundException If the specified session ID does not
	 *                                  correspond to an existing session.
	 */

	@DeleteMapping(SESSION_ID)
	public ResponseEntity<SessionResponseDto> deleteSession(@Valid @PathVariable String sessionId)
			throws SessionNotFoundException,SessionCannotBeDeleted {
		SessionResponseDto sessionResponseDto = sessionService.deleteSession(sessionId);

		return new ResponseEntity<>(sessionResponseDto, HttpStatus.OK);
	}

	/**
	 * Retrieves a list of session response DTOs based on the specified status and
	 * optional paging and sorting parameters.
	 *
	 * @param status     The status of the sessions to retrieve.
	 * @param pageNumber The page number for pagination (optional, default is 1).
	 * @param pageSize   The page size for pagination (optional, default is 10).
	 * @param sortBy     The field to sort by (optional, default is "id").
	 * @param sortDir    The sort direction ("asc" or "desc", optional, default is
	 *                   "asc").
	 * @return A list of session response DTOs with updated archive flags.
	 */

	@GetMapping(STATUS_REQ)
	public ResponseEntity<SessionListResponse> getSessionsByStatus(@PathVariable char status,
			@RequestParam(defaultValue = PAGE_NUMBER) int pageNumber,
			@RequestParam(defaultValue = PAGE_SIZE) int pageSize, @RequestParam(defaultValue = SORT_BY) String sortBy,
			@RequestParam(defaultValue = SORT_DIR) String sortDir) throws SessionNotFoundException {
		SessionListResponse sessionListResponse = sessionService.getSessions(status, pageNumber, pageSize, sortBy,
				sortDir);
		return ResponseEntity.ok(sessionListResponse);
	}

}
