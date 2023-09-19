package com.session.exception;

public class CustomerNotFoundExcpetion extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CustomerNotFoundExcpetion(String message) {
		super(message);

	}
}
