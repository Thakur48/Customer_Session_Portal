package com.session.exception;

public class SessionNotFoundByStatus extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SessionNotFoundByStatus(String msg)
	{
		super(msg);
	}

}
