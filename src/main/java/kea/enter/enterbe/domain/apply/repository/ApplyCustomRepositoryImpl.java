package kea.enter.enterbe.domain.apply.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kea.enter.enterbe.api.lottery.controller.dto.request.ApplicantSearchType;
import kea.enter.enterbe.api.lottery.controller.dto.response.GetLotteryResponse;
import kea.enter.enterbe.api.lottery.controller.dto.response.LotteryListInfo;
import kea.enter.enterbe.domain.apply.entity.Apply;
import kea.enter.enterbe.domain.apply.entity.ApplyRound;
import kea.enter.enterbe.domain.apply.entity.ApplyRoundState;
import kea.enter.enterbe.domain.apply.entity.ApplyState;
import kea.enter.enterbe.domain.lottery.entity.WinningState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static kea.enter.enterbe.domain.lottery.entity.QWinning.winning;
import static kea.enter.enterbe.domain.apply.entity.QApply.apply;
import static kea.enter.enterbe.domain.apply.entity.QApplyRound.applyRound;
import static kea.enter.enterbe.domain.member.entity.QMember.member;

@Slf4j
@RequiredArgsConstructor
public class ApplyCustomRepositoryImpl implements ApplyCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Apply> findAllApplyByCondition(Long applyRoundId, String keyword, ApplicantSearchType searchType, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        builder
            .and(apply.applyRound.id.eq(applyRoundId))
            .and(apply.state.eq(ApplyState.ACTIVE));

        // 검색 조건에 따른 쿼리 처리
        BooleanBuilder searchBuilder = new BooleanBuilder();
        switch (searchType) {
            case ALL -> searchBuilder.or(idContains(searchType, keyword))
                .or(nameContains(searchType, keyword));
            case ID -> searchBuilder.and(idContains(searchType, keyword));
            case NAME -> searchBuilder.and(nameContains(searchType, keyword));
        }

        builder.and(searchBuilder);

        // 조건에 맞는 신청 내역 조회
        List<Apply> applyList = jpaQueryFactory
            .select(apply)
            .from(apply)
            .where(builder)
            .leftJoin(apply.member, member).fetchJoin()
            .orderBy(apply.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        // 전체 항목 수 쿼리
        List<Long> total = jpaQueryFactory
            .select(apply.id)
            .from(apply)
            .where(builder)
            .fetch();

        return PageableExecutionUtils.getPage(applyList, pageable, total::size);
    }

    public Page<LotteryListInfo> findAllLotteryResponsebyId(Pageable pageable, Long memberId) {
        List<LotteryListInfo> responses = new ArrayList<>();

        List<ApplyRound> applyRounds = jpaQueryFactory.selectFrom(applyRound)
            .innerJoin(apply).on(applyRound.id.eq(apply.applyRound.id))
            .innerJoin(member).on(apply.member.id.eq(member.id))
            .where(apply.state.eq(ApplyState.ACTIVE)
                .and(applyRound.state.eq(ApplyRoundState.ACTIVE)
                    .and(member.id.eq(memberId))))
            .orderBy(applyRound.id.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long count = jpaQueryFactory.selectFrom(applyRound)
            .innerJoin(apply).on(applyRound.id.eq(apply.applyRound.id))
            .innerJoin(member).on(apply.member.id.eq(member.id))
            .where(apply.state.eq(ApplyState.ACTIVE)
                .and(applyRound.state.eq(ApplyRoundState.ACTIVE)
                    .and(member.id.eq(memberId))))
            .fetchCount();

        for (ApplyRound round : applyRounds) {
            // 경쟁률
            long applyCount = jpaQueryFactory
                .selectFrom(apply)
                .innerJoin(applyRound).on(apply.applyRound.id.eq(applyRound.id))
                .where(apply.state.eq(ApplyState.ACTIVE)
                    .and(applyRound.id.eq(round.getId())))
                .fetchCount();

            // 당첨 여부
            String result = jpaQueryFactory
                .select(
                    new CaseBuilder()
                        .when(winning.state.eq(WinningState.ACTIVE)).then("당첨")
                        .when(winning.state.eq(WinningState.INACTIVE)).then("당첨취소")
                        .otherwise("미당첨")
                )
                .from(apply)
                .leftJoin(winning).on(winning.apply.id.eq(apply.id))
                .where(
                    apply.state.eq(ApplyState.ACTIVE)
                        .and(apply.member.id.eq(memberId)
                            .and(applyRound.id.eq(round.getId())))
                )
                .fetchFirst();

            responses.add(LotteryListInfo.builder()
                .round(round.getRound())
                .takeDate(round.getTakeDate().format(DateTimeFormatter.ofPattern("MM-dd")))
                .returnDate(round.getReturnDate().format(DateTimeFormatter.ofPattern("MM-dd")))
                .competitionRate(applyCount)
                .result(result)
                .build());
        }

        return new PageImpl<>(responses, pageable, count);
    }


    // 사용자 아이디(이메일) 검색
    private BooleanExpression idContains(ApplicantSearchType searchType, String keyword) {
        if(searchType == ApplicantSearchType.NAME) return null;
        return keyword != null ?
            apply.member.email.toUpperCase().contains(keyword)
                .or(apply.member.email.toLowerCase().contains(keyword)) : null;
    }

    // 사용자 이름 검색
    private BooleanExpression nameContains(ApplicantSearchType searchType, String keyword) {
        if(searchType == ApplicantSearchType.ID) return null;
        return keyword != null ?
            apply.member.name.toUpperCase().contains(keyword)
                .or(apply.member.name.toLowerCase().contains(keyword)): null;
    }
}