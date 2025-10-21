package com.curso.gameapi.repository;

import com.curso.gameapi.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Integer> { }