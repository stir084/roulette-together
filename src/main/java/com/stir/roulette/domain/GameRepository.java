package com.stir.roulette.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {

    @Query("SELECT p FROM Game p ORDER BY p.id DESC")
    Optional<Game> findByIdentifiedGameCode(String identifiedGameCode);
    Optional<Game> find
}