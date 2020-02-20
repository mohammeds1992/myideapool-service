package com.backend.services.myideapool.exceptions;

public class BadJWTTokenException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BadJWTTokenException() {
        super("Bad JWT token has been given");
    }
}