package com.curso.gameapi.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_player")
    @SequenceGenerator(name = "seq_player", sequenceName = "SEQ_PLAYER", allocationSize = 1)
    private Integer idPlayer;

    private String nome;

    @ManyToOne
    @JoinColumn(name = "game_fav_id")
    private Game gameFav;
}
