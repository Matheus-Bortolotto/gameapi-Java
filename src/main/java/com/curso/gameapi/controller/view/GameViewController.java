package com.curso.gameapi.controller.view;

import com.curso.gameapi.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/games")
public class GameViewController {

    private final GameService service;

    public GameViewController(GameService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("games", service.listAll());
        return "games"; // => resources/templates/games.html
    }
}
