package com.gentics.kitchenoffice.exception;

public class UserExistsAlreadyException extends IllegalArgumentException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2159718750933252402L;

	public UserExistsAlreadyException() {
		super();
	}

	public UserExistsAlreadyException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserExistsAlreadyException(String s) {
		super(s);
	}

	public UserExistsAlreadyException(Throwable cause) {
		super(cause);
	}

}
