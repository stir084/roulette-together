package com.stir.roulette.repository;

import com.stir.roulette.domain.Roulette;
import com.stir.roulette.domain.RouletteSegment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouletteSegmentRepository extends JpaRepository<RouletteSegment, Long> {
}