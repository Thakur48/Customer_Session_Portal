package com.session.service;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.session.dto.EditSessionRequestDto;
import com.session.dto.SessionListResponse;
import com.session.dto.SessionRequestDto;
import com.session.dto.SessionResponseDto;
import com.session.exception.CustomerNotFoundExcpetion;
import com.session.exception.SessionCannotBeDeleted;
import com.session.exception.SessionCantBeArchivedException;
import com.session.exception.SessionNotActiveException;
import com.session.exception.SessionNotFoundException;

@Service
@Validated
public interface ISessionService {

	public SessionResponseDto createSession(@Valid SessionRequestDto sessionRequestDto)
			throws CustomerNotFoundExcpetion;

	public SessionResponseDto archiveTheSession(@Valid String sessionId)
			throws SessionCantBeArchivedException, SessionNotFoundException,SessionNotActiveException;

	public SessionResponseDto editActiveSession(@Valid String sessionId, EditSessionRequestDto editSessionRequestDto)
			throws SessionNotFoundException, SessionNotActiveException;

	public SessionResponseDto deleteSession(@Valid String sessionId) throws SessionNotFoundException,SessionCannotBeDeleted;

	public SessionListResponse getSessions(char status, int pageNumber, int pageSize, String sortBy, String sortDir)
			throws SessionNotFoundException;

}
