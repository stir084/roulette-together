package com.stir.roulette.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.stir.roulette.domain.QRoulette;
import com.stir.roulette.domain.Roulette;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class RouletteRepositoryImpl implements RouletteRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    //QuerydslRepositorySupport이 없으므로 그에따른 생성자 필요없음

    QRoulette roulette = QRoulette.roulette;
   // QMember member = QMember.member;

    //1. queryDsl 기본 쿼리
   /* public List<Member> findByName(String name) {
        return queryFactory.selectFrom(member)
                .where(member.name.eq(name))
                .fetch();
    }*/

    public List<Roulette> findLastGame(String userIp) {
        return queryFactory.selectFrom(roulette)
                .where(roulette.user.userIp.eq(userIp))
                .orderBy(roulette.id.desc())
                .offset(0).limit(1)
                .fetch();
    }
}
