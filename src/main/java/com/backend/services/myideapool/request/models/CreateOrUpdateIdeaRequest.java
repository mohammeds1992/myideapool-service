package com.backend.services.myideapool.request.models;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;

@Getter
public class CreateOrUpdateIdeaRequest {

    @NotBlank
    @Size(max = 255, min = 1)
    private String content;

    @NotNull
    @Max(10)
    @Min(1)
    private Integer impact;

    @NotNull
    @Max(10)
    @Min(1)
    private Integer ease;

    @NotNull
    @Max(10)
    @Min(1)
    private Integer confidence;
}