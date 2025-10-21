package com.curso.gameapi.dto;

import jakarta.validation.constraints.*;

public record GameRequest(
        @NotBlank @Size(max = 100) String titulo,
        @Size(max = 80) String editora,
        @Size(max = 60) String genero,
        @NotNull @Min(1950) @Max(2100) Integer anoLancamento
) { }