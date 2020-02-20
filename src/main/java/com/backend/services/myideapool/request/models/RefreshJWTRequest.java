package com.backend.services.myideapool.request.models;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class RefreshJWTRequest {

	@NotBlank
	private String refresh_token;

}