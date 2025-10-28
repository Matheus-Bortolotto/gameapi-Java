package com.curso.gameapi.controller.view;

import com.curso.gameapi.service.PlayerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/players")
public class PlayerViewController {

    private final PlayerService service;

    public PlayerViewController(PlayerService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("players", service.listAll());
        return "players"; // => resources/templates/players.html
    }
}
