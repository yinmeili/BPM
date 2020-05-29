package com.h3bpm.web.service;


/**
 * This exception will be thrown when some errors occur in the platform layer.
 * 
 * @author Eric Feng
 */
public class TransportException extends Exception {
	private static final long serialVersionUID = 2390553120295672031L;

	private int errorCode = 0;

	public TransportException(int errorCode, String message, Throwable cause) {
		super(message, cause);

		this.errorCode = errorCode;
	}

	public TransportException(int errorCode, String message) {
		this(errorCode, message, null);
	}

	public TransportException(String message, Throwable cause) {
		super(message, cause);
	}

	public TransportException(String message) {
		this(message, null);
	}

	public TransportException(Exception ex) {
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
