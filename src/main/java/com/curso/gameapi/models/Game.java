package com.curso.gameapi.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_game")
    @SequenceGenerator(name = "seq_game", sequenceName = "SEQ_GAME", allocationSize = 1)
    private Integer idGame;

    private String titulo;
    private String editora;
    private String genero;
    private Integer anoLancamento;
}
