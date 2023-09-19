package com.session.exception;

public class SessionCantBeArchivedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SessionCantBeArchivedException(String msg) {
		super(msg);
	}

}
