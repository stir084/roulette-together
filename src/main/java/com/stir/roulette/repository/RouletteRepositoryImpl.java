package com.stir.roulette.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.stir.roulette.domain.QRoulette;
import com.stir.roulette.domain.Roulette;
import com.stir.roulette.domain.RouletteStatus;
import com.stir.roulette.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.stir.roulette.domain.QRouletteSegment.rouletteSegment;

@RequiredArgsConstructor
public class RouletteRepositoryImpl implements RouletteRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    //QuerydslRepositorySupport이 없으므로 그에따른 생성자 필요없음

    QRoulette roulette = QRoulette.roulette; // static으로 얘도 제거 가능
   // QMember member = QMember.member;

    //1. queryDsl 기본 쿼리
   /* public List<Member> findByName(String name) {
        return queryFactory.selectFrom(member)
                .where(member.name.eq(name))
                .fetch();
    }*/

    public List<Roulette> findLastGameByUserUUID(String userIp) {
        return queryFactory.selectFrom(roulette)
                .where(roulette.user.userUUID.eq(userIp))
                .orderBy(roulette.id.desc())
                .offset(0).limit(1)
                .fetch();
    }

    public Page<Roulette> findByUserAndStatus(User user, RouletteStatus rouletteStatus, Pageable pageable){
        List<Roulette> content = queryFactory.selectFrom(roulette).distinct()
                .innerJoin(roulette.rouletteSegments, rouletteSegment)
                //.fetchJoin() //일대다조인에서 페이징 + 페치 금지 - 이거빼니까 데이터 이상하게 나옴 - 뻥튀기 돼서 그럼. distinct로 해결
                .where(roulette.user.userUUID.eq(user.getUserUUID()), roulette.status.eq(rouletteStatus))
                .orderBy(roulette.id.desc(), rouletteSegment.id.asc())
                .offset(pageable.getOffset())   // (2) 페이지 번호
                .limit(pageable.getPageSize())  // (3) 페이지 사이즈
                .fetch();

        Long count = queryFactory		// (4)
                .select(roulette.count())
                .from(roulette)
//                .leftJoin(member.team, team)		(5) 검색조건 최적화
                .where(roulette.user.userUUID.eq(user.getUserUUID()), roulette.status.eq(rouletteStatus))
                .fetchOne();

        return new PageImpl<>(content, pageable, count);
    }

  /*  public Page<Roulette> findByUserAndStatus2(User user, RouletteStatus rouletteStatus, Pageable pageable){
        List<Roulette> content = queryFactory.selectFrom(roulette)
                .innerJoin(roulette.rouletteSegments, rouletteSegment)
                .fetchJoin() //일대다조인에서 페이징 + 페치 금지
                .where(roulette.user.userIp.eq(user.getUserIp()), roulette.status.eq(rouletteStatus))
                .orderBy(roulette.id.desc(), rouletteSegment.id.asc())
                .offset(pageable.getOffset())   // (2) 페이지 번호
                .limit(pageable.getPageSize())  // (3) 페이지 사이즈
                .fetch();

        Long count = queryFactory		// (4)
                .select(roulette.count())
                .from(roulette)
//                .leftJoin(member.team, team)		(5) 검색조건 최적화
                .where(roulette.user.userIp.eq(user.getUserIp()), roulette.status.eq(rouletteStatus))
                .fetchOne();

        return new PageImpl<>(content, pageable, count);
    }
*/
   // (User user, RouletteStatus rouletteStatus, Pageable pageable);
    /*public Roulette findByRouletteUID(Long id, UUID rouletteUID) {
        return queryFactory.selectFrom(roulette)
                .join(roulette.rouletteSegments, rouletteSegment)
                .where(roulette.id.eq(id), roulette.rouletteUID.eq(rouletteUID))
                .fetchOne();
    }*/
}
