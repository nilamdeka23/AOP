package edu.sjsu.cmpe275.aop.exceptions;

public class NetworkException extends Exception {
	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 2L;

	public NetworkException(String message) {
		super(message);
	}

	public NetworkException(String message, Throwable throwable) {
		super(message, throwable);
	}
}