package edu.sjsu.cmpe275.aop.exceptions;

public class AccessDeniedExeption extends Exception {
	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;

	public AccessDeniedExeption(String message) {
		super(message);
	}

	public AccessDeniedExeption(String message, Throwable throwable) {
		super(message, throwable);
	}
}