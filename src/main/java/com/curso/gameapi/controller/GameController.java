package com.curso.gameapi.controller;

import com.curso.gameapi.dto.GameRequest;
import com.curso.gameapi.dto.GameResponse;
import com.curso.gameapi.service.GameService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService service;

    public GameController(GameService service) {
        this.service = service;
    }

    @GetMapping
    public List<GameResponse> listAll() {
        return service.listAll();
    }

    @PostMapping
    public GameResponse create(@RequestBody GameRequest request) {
        return service.create(request);
    }

    @GetMapping("/{id}")
    public GameResponse getOne(@PathVariable Long id) {
        return service.getOne(id);
    }
}
