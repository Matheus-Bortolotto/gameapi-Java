package com.curso.gameapi.controller;

import com.curso.gameapi.dto.GameRequest;
import com.curso.gameapi.dto.GameResponse;
import com.curso.gameapi.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/games")
@Tag(name = "Games", description = "CRUD de Games")
public class GameController {

    private final GameService service;

    public GameController(GameService service) {
        this.service = service;
    }

    @Operation(summary = "Lista todos os games")
    @GetMapping
    public List<GameResponse> list() {
        return service.listAll();
    }

    @Operation(summary = "Busca game por id")
    @GetMapping("/{id}")
    public ResponseEntity<GameResponse> get(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "Cria um novo game")
    @PostMapping
    public ResponseEntity<GameResponse> create(@Valid @RequestBody GameRequest request) {
        GameResponse created = service.create(request);
        URI location = URI.create("/api/games/" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Atualiza um game")
    @PutMapping("/{id}")
    public ResponseEntity<GameResponse> update(@PathVariable Integer id,
                                               @Valid @RequestBody GameRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(summary = "Exclui um game")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
