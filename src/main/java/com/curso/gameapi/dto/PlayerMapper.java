package com.curso.gameapi.dto;

import com.curso.gameapi.models.Game;
import com.curso.gameapi.models.Player;

public class PlayerMapper {

    public static Player toEntity(PlayerRequest req, Game gameFav) {
        Player p = new Player();
        p.setNome(req.nome());
        p.setGameFav(gameFav);
        return p;
    }

    public static PlayerResponse toResponse(Player p) {
        Integer gameId = p.getGameFav() != null ? p.getGameFav().getIdGame() : null;
        return new PlayerResponse(
                p.getIdPlayer(),
                p.getNome(),
                gameId
        );
    }

    public static void update(Player entity, PlayerRequest req, Game gameFav) {
        entity.setNome(req.nome());
        entity.setGameFav(gameFav);
    }
}
