package com.gentics.kitchenoffice.exception;

public class EmailExistsAlreadyException extends IllegalArgumentException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4612275661129293059L;

	public EmailExistsAlreadyException() {
		super();
	}

	public EmailExistsAlreadyException(String message, Throwable cause) {
		super(message, cause);
	}

	public EmailExistsAlreadyException(String s) {
		super(s);
	}

	public EmailExistsAlreadyException(Throwable cause) {
		super(cause);
	}

}
