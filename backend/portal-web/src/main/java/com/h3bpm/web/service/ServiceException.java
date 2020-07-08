package com.h3bpm.web.service;

/**
 * This exception will be thrown when some errors occur in the platform layer.
 * 
 * @author Tonghao
 */
public class ServiceException extends Exception {
	private static final long serialVersionUID = 2390553120295672021L;

	private int errorCode = 0;

	public ServiceException(int errorCode, String message, Throwable cause) {
		super(message, cause);

		this.errorCode = errorCode;
	}

	public ServiceException(int errorCode, String message) {
		this(errorCode, message, null);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceException(String message) {
		this(message, null);
	}

	public ServiceException(Exception ex) {
		this(ex.getMessage(), ex.getCause());
	}

	public int getErrorCode() {
		return errorCode;
	}

	@Override
	public String toString() {
		return "[errorCode=" + getErrorCode() + ",message=" + getMessage() + "]";
	}
}
