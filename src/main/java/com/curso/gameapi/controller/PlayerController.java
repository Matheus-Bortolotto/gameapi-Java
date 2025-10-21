package com.curso.gameapi.controller;

import com.curso.gameapi.dto.PlayerMapper;
import com.curso.gameapi.dto.PlayerRequest;
import com.curso.gameapi.dto.PlayerResponse;
import com.curso.gameapi.models.Game;
import com.curso.gameapi.models.Player;
import com.curso.gameapi.repository.GameRepository;
import com.curso.gameapi.repository.PlayerRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/players")
@Tag(name = "Players", description = "CRUD de Players")
public class PlayerController {

    private final PlayerRepository playerRepository;
    private final GameRepository gameRepository;

    public PlayerController(PlayerRepository playerRepository, GameRepository gameRepository) {
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
    }

    @Operation(summary = "Lista todos os players")
    @GetMapping
    public List<PlayerResponse> list() {
        return playerRepository.findAll().stream()
                .map(PlayerMapper::toResponse)
                .collect(toList());
    }

    @Operation(summary = "Busca player por id")
    @GetMapping("/{id}")
    public ResponseEntity<PlayerResponse> get(@PathVariable Integer id) {
        return playerRepository.findById(id)
                .map(PlayerMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Cria um novo player")
    @PostMapping
    public ResponseEntity<PlayerResponse> create(@Valid @RequestBody PlayerRequest request) {
        Game fav = null;
        if (request.gameFavId() != null) {
            fav = gameRepository.findById(request.gameFavId())
                    .orElseThrow(() -> new IllegalArgumentException("Game favorito não encontrado: " + request.gameFavId()));
        }
        Player saved = playerRepository.save(PlayerMapper.toEntity(request, fav));
        URI location = URI.create("/api/players/" + saved.getIdPlayer());
        return ResponseEntity.created(location).body(PlayerMapper.toResponse(saved));
    }

    @Operation(summary = "Atualiza um player")
    @PutMapping("/{id}")
    public ResponseEntity<PlayerResponse> update(@PathVariable Integer id, @Valid @RequestBody PlayerRequest request) {
        return playerRepository.findById(id)
                .map(existing -> {
                    Game fav = null;
                    if (request.gameFavId() != null) {
                        fav = gameRepository.findById(request.gameFavId())
                                .orElseThrow(() -> new IllegalArgumentException("Game favorito não encontrado: " + request.gameFavId()));
                    }
                    PlayerMapper.update(existing, request, fav);
                    Player saved = playerRepository.save(existing);
                    return ResponseEntity.ok(PlayerMapper.toResponse(saved));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Exclui um player")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!playerRepository.existsById(id)) return ResponseEntity.notFound().build();
        playerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
