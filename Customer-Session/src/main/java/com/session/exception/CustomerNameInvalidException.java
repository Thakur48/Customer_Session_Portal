package com.session.exception;

public class CustomerNameInvalidException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomerNameInvalidException(String msg) {
		super(msg);
	}

}
