package com.stir.roulette.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameInfoRepository extends JpaRepository<Game, Long> {
}