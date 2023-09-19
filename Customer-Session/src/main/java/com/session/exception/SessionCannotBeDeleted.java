package com.session.exception;

public class SessionCannotBeDeleted extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SessionCannotBeDeleted(String msg) {
		super(msg);
	}

}
