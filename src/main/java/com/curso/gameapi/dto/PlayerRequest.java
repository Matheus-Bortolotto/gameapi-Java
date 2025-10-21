package com.curso.gameapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PlayerRequest(
        @NotBlank @Size(max = 100) String nome,
        Integer gameFavId
) { }
