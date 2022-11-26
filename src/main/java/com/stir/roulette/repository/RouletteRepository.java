package com.stir.roulette.repository;

import com.stir.roulette.domain.Roulette;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface RouletteRepository extends JpaRepository<Roulette, Long>, RouletteRepositoryCustom {
    Optional<Roulette> findByRouletteUID(UUID rouletteUID);

    //Page<Roulette> findByUserAndStatus(User user, RouletteStatus rouletteStatus, Pageable pageable);


}
