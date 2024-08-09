package kea.enter.enterbe.domain.lottery.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kea.enter.enterbe.api.take.controller.dto.request.ReportSearchType;
import kea.enter.enterbe.domain.lottery.entity.Winning;
import kea.enter.enterbe.domain.lottery.entity.WinningState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static kea.enter.enterbe.domain.apply.entity.QApply.apply;
import static kea.enter.enterbe.domain.apply.entity.QApplyRound.applyRound;
import static kea.enter.enterbe.domain.vehicle.entity.QVehicle.vehicle;
import static kea.enter.enterbe.domain.lottery.entity.QWinning.winning;

@Slf4j
@RequiredArgsConstructor
public class WinningCustomRepositoryImpl implements WinningCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Winning> findAllWinningByCondition(String keyword, ReportSearchType searchType, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(winning.state.eq(WinningState.ACTIVE));

        // 검색 조건에 따른 쿼리 처리
        BooleanBuilder searchBuilder = new BooleanBuilder();
        switch (searchType) {
            case ALL ->
                searchBuilder.or(nameContains(searchType, keyword))
                    .or(vehicleModelContains(searchType, keyword))
                    .or(vehicleNoContains(searchType, keyword));
            case MEMBER ->
                searchBuilder.and(nameContains(searchType, keyword));
            case VEHICLE ->
                searchBuilder.or(vehicleModelContains(searchType, keyword))
                    .or(vehicleNoContains(searchType, keyword));
        }

        builder.and(searchBuilder);

        // 조건에 맞는 당첨 내역 조회
        List<Winning> winningList = jpaQueryFactory
            .selectFrom(winning)
            .leftJoin(winning.apply, apply).fetchJoin()
            .leftJoin(apply.applyRound, applyRound).fetchJoin()
            .leftJoin(applyRound.vehicle, vehicle).fetchJoin() // 이 부분을 추가
            .where(builder)
            .orderBy(winning.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        // 전체 항목 수 쿼리
        long total = jpaQueryFactory
            .select(winning.id.count())
            .from(winning)
            .leftJoin(winning.apply, apply)
            .leftJoin(apply.applyRound, applyRound)
            .leftJoin(applyRound.vehicle, vehicle)
            .where(builder)
            .fetchOne();

        return PageableExecutionUtils.getPage(winningList, pageable, () -> total);
    }

    // 사용자 이름 검색
    private BooleanExpression nameContains(ReportSearchType searchType, String keyword) {
        if (searchType != ReportSearchType.MEMBER && searchType != ReportSearchType.ALL) return null;
        return keyword != null ? apply.member.name.contains(keyword) : null;
    }

    // 차량 모델 검색
    private BooleanExpression vehicleModelContains(ReportSearchType searchType, String keyword) {
        if (searchType != ReportSearchType.VEHICLE && searchType != ReportSearchType.ALL) return null;
        return keyword != null ? vehicle.model.contains(keyword) : null;
    }

    // 차량 번호 검색
    private BooleanExpression vehicleNoContains(ReportSearchType searchType, String keyword) {
        if (searchType != ReportSearchType.VEHICLE && searchType != ReportSearchType.ALL) return null;
        return keyword != null ? vehicle.vehicleNo.contains(keyword) : null;
    }

}