package com.stir.roulette.repository;

import com.stir.roulette.domain.Roulette;
import com.stir.roulette.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RouletteRepository extends JpaRepository<Roulette, Long> {
    /*Optional<Game> findTopByUserIpOrderByIdDesc(String ip); 이것도 외래키 이상하게 들고온거지 머*/
    List<Roulette> findByUser(User user);

    @Query("SELECT p FROM Roulette p ORDER BY p.id DESC")
    List<Roulette> findByTopGame(Pageable pageable);
}