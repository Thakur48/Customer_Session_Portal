package com.session.exception;

public class SessionNotActiveException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SessionNotActiveException(String msg) {
		super(msg);
	}

}
