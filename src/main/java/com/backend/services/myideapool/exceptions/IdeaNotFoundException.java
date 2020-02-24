package com.backend.services.myideapool.exceptions;

public class IdeaNotFoundException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public IdeaNotFoundException(Integer id) {
		super("Idea with id: " + id + " not found");
	}
}