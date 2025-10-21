package com.curso.gameapi.dto;

public record GameResponse(
        Integer id,
        String titulo,
        String editora,
        String genero,
        Integer anoLancamento
) { }