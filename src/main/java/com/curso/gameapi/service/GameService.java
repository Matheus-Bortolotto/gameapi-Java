package com.curso.gameapi.service;

import com.curso.gameapi.dto.GameRequest;
import com.curso.gameapi.dto.GameResponse;

import java.util.List;

public interface GameService {
    List<GameResponse> listAll();
    GameResponse getById(Integer id);
    GameResponse create(GameRequest request);
    GameResponse update(Integer id, GameRequest request);
    void delete(Integer id);
}
