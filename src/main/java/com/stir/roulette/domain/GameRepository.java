package com.stir.roulette.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    /*Optional<Game> findTopByUserIpOrderByIdDesc(String ip); 이것도 외래키 이상하게 들고온거지 머*/
    List<Game> findByUser(User user);

    @Query("SELECT p FROM Game p ORDER BY p.id DESC")
    List<Game> findByTopGame(Pageable pageable);
}