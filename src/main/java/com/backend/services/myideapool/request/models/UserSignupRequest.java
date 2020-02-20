package com.backend.services.myideapool.request.models;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class UserSignupRequest {

	@NotBlank
	private String name;

	@NotBlank
	private String email;

	@NotBlank
	private String password;
}