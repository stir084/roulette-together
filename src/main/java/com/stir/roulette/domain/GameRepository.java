package com.stir.roulette.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findTopByUserIpOrderByIdDesc(String ip);
    List<Game> findByUser(User user);
}