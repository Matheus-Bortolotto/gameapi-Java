package com.curso.gameapi.repository;

import com.curso.gameapi.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Integer> { }
