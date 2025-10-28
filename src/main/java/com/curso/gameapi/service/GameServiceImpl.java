package com.curso.gameapi.service;

import com.curso.gameapi.dto.GameMapper;
import com.curso.gameapi.dto.GameRequest;
import com.curso.gameapi.dto.GameResponse;
import com.curso.gameapi.exception.ResourceNotFoundException;
import com.curso.gameapi.models.Game;
import com.curso.gameapi.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<GameResponse> listAll() {
        return repository.findAll()
                .stream()
                .map(GameMapper::toResponse)
                .collect(toList());
    }

    @Override
    @Transactional(readOnly = true)
    public GameResponse getById(Integer id) {
        Game game = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Game não encontrado: id=" + id));
        return GameMapper.toResponse(game);
    }

    @Override
    @Transactional
    public GameResponse create(GameRequest request) {
        Game entity = GameMapper.toEntity(request);
        Game saved = repository.save(entity);
        return GameMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public GameResponse update(Integer id, GameRequest request) {
        Game existing = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Game não encontrado: id=" + id));

        // reaproveita o mapper pra popular os campos,
        // mas mantém o mesmo ID do registro existente
        Game updated = GameMapper.toEntity(request);
        updated.setIdGame(existing.getIdGame());

        Game saved = repository.save(updated);
        return GameMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Game não encontrado: id=" + id);
        }
        repository.deleteById(id);
    }
}
