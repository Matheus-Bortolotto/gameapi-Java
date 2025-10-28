package com.curso.gameapi.service;

import com.curso.gameapi.dto.PlayerRequest;
import com.curso.gameapi.dto.PlayerResponse;

import java.util.List;

public interface PlayerService {
    List<PlayerResponse> listAll();
    PlayerResponse getById(Integer id);
    PlayerResponse create(PlayerRequest request);
    PlayerResponse update(Integer id, PlayerRequest request);
    void delete(Integer id);
}
