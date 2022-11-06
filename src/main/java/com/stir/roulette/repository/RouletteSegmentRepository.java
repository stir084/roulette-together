package com.stir.roulette.repository;

import com.stir.roulette.domain.Roulette;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouletteSegmentRepository extends JpaRepository<Roulette, Long> {
}