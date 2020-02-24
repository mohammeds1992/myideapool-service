package com.backend.services.myideapool.exceptions;

public class DuplicateEmailFoundException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DuplicateEmailFoundException(String email) {
        super("The email: " + email + " is already taken");
    }
}
