package com.curso.gameapi.dto;

public record PlayerResponse(
        Integer id,
        String nome,
        Integer gameFavId
) { }
