package com.curso.gameapi.controller;

import com.curso.gameapi.dto.PlayerRequest;
import com.curso.gameapi.dto.PlayerResponse;
import com.curso.gameapi.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/players")
@Tag(name = "Players", description = "CRUD de Players")
public class PlayerController {

    private final PlayerService service;

    public PlayerController(PlayerService service) {
        this.service = service;
    }

    @Operation(summary = "Lista todos os players")
    @GetMapping
    public List<PlayerResponse> list() {
        return service.listAll();
    }

    @Operation(summary = "Busca player por id")
    @GetMapping("/{id}")
    public ResponseEntity<PlayerResponse> get(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "Cria um novo player")
    @PostMapping
    public ResponseEntity<PlayerResponse> create(@Valid @RequestBody PlayerRequest request) {
        PlayerResponse created = service.create(request);
        URI location = URI.create("/api/players/" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Atualiza um player")
    @PutMapping("/{id}")
    public ResponseEntity<PlayerResponse> update(@PathVariable Integer id,
                                                 @Valid @RequestBody PlayerRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(summary = "Exclui um player")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
