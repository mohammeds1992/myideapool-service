package com.backend.services.myideapool.request.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class CreateOrUpdateIdeaRequest {

	@NotBlank
	private String content;

	@NotNull
	private Integer impact;

	@NotNull
	private Integer ease;

	@NotNull
	private Integer confidence;
}