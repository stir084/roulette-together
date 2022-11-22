package com.stir.roulette.service;

import com.stir.roulette.config.ConfigBean;
import com.stir.roulette.domain.Roulette;
import com.stir.roulette.domain.RouletteSegment;
import com.stir.roulette.domain.RouletteStatus;
import com.stir.roulette.domain.User;
import com.stir.roulette.repository.RouletteRepository;
import com.stir.roulette.repository.RouletteSegmentRepository;
import com.stir.roulette.repository.UserRepository;
import com.stir.roulette.web.dto.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class RouletteSegmentService {

    private final RouletteSegmentRepository rouletteSegmentRepository;

    @PersistenceContext
    public EntityManager em;

    @Transactional
    public void deleteSegment(UUID segmentUID) {
        //Roulette Status가 종료인경우 하면안됨.
        RouletteSegment rouletteSegment = rouletteSegmentRepository.findBySegmentUID(segmentUID)
                .orElseThrow(() -> new IllegalArgumentException("조회된 내역이 없습니다"));

        Roulette roulette = rouletteSegment.getRoulette();
        if(roulette.getStatus()==RouletteStatus.FINISH){
            throw new IllegalArgumentException("이미 종료된 게임입니다.");
        }
        if(roulette.getRouletteSegments().size() <= 1){
            throw new IllegalArgumentException("최소 1개의 아이템은 있어야 합니다.");
        };
        //rouletteSegment.getRoulette().getRouletteSegments().remove(0); // 고아 객체 생성
        em.clear(); //Segement 리스트 1차 캐시 저장에 따른 delete 기능 동작 방해로 인해 1차 캐시 clear

        rouletteSegmentRepository.delete(rouletteSegment);
    }
}