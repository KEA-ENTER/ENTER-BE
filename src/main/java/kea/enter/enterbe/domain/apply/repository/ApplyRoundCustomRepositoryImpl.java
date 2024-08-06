package kea.enter.enterbe.domain.apply.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kea.enter.enterbe.api.lottery.controller.dto.request.LotterySearchType;
import kea.enter.enterbe.domain.apply.entity.ApplyRound;
import kea.enter.enterbe.domain.apply.entity.ApplyRoundState;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static kea.enter.enterbe.domain.apply.entity.QApplyRound.applyRound1;
import static kea.enter.enterbe.domain.vehicle.entity.QVehicle.vehicle;

@Slf4j
@RequiredArgsConstructor
public class ApplyRoundCustomRepositoryImpl implements ApplyRoundCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<ApplyRound> findAllApplyRoundByCondition(String keyword, LotterySearchType searchType, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        builder
            .or(vehicleNoContains(searchType, keyword))
            .and(applyRound1.state.eq(ApplyRoundState.ACTIVE));

        // 검색 조건에 따른 쿼리 처리
        BooleanBuilder searchBuilder = new BooleanBuilder();
        switch (searchType) {
            case ALL -> searchBuilder.or(roundContains(searchType, keyword))
                .or(vehicleModelContains(searchType, keyword))
                .or(vehicleNoContains(searchType, keyword));

            case VEHICLE -> searchBuilder.or(vehicleModelContains(searchType, keyword))
                .or(vehicleNoContains(searchType, keyword));

            case ROUND -> searchBuilder.and(roundContains(searchType, keyword));
        }

        builder.and(searchBuilder);

        // 조건에 맞는 신청 회차 조회
        List<ApplyRound> applyRoundList = jpaQueryFactory
            .select(applyRound1)
            .from(applyRound1)
            .where(builder)
            .leftJoin(applyRound1.vehicle, vehicle).fetchJoin()
            .orderBy(applyRound1.takeDate.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        // 전체 항목 수 쿼리
        List<Long> total = jpaQueryFactory
            .select(applyRound1.id)
            .from(applyRound1)
            .where(builder)
            .fetch();

        return PageableExecutionUtils.getPage(applyRoundList, pageable, total::size);
    }

    // 신청 회차 검색
    private BooleanExpression roundContains(LotterySearchType searchType, String keyword) {
        if(searchType == LotterySearchType.VEHICLE) return null;
        try {
            return keyword != null ? applyRound1.applyRound.eq(Integer.parseInt(keyword)) : null;
        } catch (NumberFormatException e) {
            // 전체 검색 시 키워드가 정수형이 아니면 null, 회차 검색 시 키워드가 정수형이 아니면 예외를 반환한다
            if(searchType == LotterySearchType.ALL) return null;
            throw new CustomException(ResponseCode.APPLY_ROUND_INVALID_SEARCH);
        }
    }

    // 차량 검색
    private BooleanExpression vehicleModelContains(LotterySearchType searchType, String keyword) {
        if(searchType == LotterySearchType.ROUND) return null;
        return keyword != null ? applyRound1.vehicle.model.contains(keyword) : null;
    }

    private BooleanExpression vehicleNoContains(LotterySearchType searchType, String keyword) {
        if(searchType == LotterySearchType.ROUND) return null;
        return keyword != null ? applyRound1.vehicle.vehicleNo.contains(keyword) : null;
    }
}
