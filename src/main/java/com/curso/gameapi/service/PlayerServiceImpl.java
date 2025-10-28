package com.curso.gameapi.service;

import com.curso.gameapi.dto.PlayerMapper;
import com.curso.gameapi.dto.PlayerRequest;
import com.curso.gameapi.dto.PlayerResponse;
import com.curso.gameapi.exception.ResourceNotFoundException;
import com.curso.gameapi.models.Game;
import com.curso.gameapi.models.Player;
import com.curso.gameapi.repository.GameRepository;
import com.curso.gameapi.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final GameRepository gameRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PlayerResponse> listAll() {
        return playerRepository.findAll()
                .stream()
                .map(PlayerMapper::toResponse)
                .collect(toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PlayerResponse getById(Integer id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Player não encontrado: id=" + id));
        return PlayerMapper.toResponse(player);
    }

    @Override
    @Transactional
    public PlayerResponse create(PlayerRequest request) {
        Game fav = resolveFavorite(request.gameFavId());
        Player entity = PlayerMapper.toEntity(request, fav);
        Player saved = playerRepository.save(entity);
        return PlayerMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public PlayerResponse update(Integer id, PlayerRequest request) {
        Player existing = playerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Player não encontrado: id=" + id));

        Game fav = resolveFavorite(request.gameFavId());
        PlayerMapper.update(existing, request, fav);

        Player saved = playerRepository.save(existing);
        return PlayerMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!playerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Player não encontrado: id=" + id);
        }
        playerRepository.deleteById(id);
    }

    private Game resolveFavorite(Integer gameFavId) {
        if (gameFavId == null) return null;
        return gameRepository.findById(gameFavId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Game favorito não encontrado: id=" + gameFavId));
    }
}
