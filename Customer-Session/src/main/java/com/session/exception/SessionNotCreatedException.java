package com.session.exception;

public class SessionNotCreatedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SessionNotCreatedException(String message) {
		super(message);
	}

}
