package kea.enter.enterbe.domain.apply.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kea.enter.enterbe.api.lottery.controller.dto.request.ApplicantSearchType;
import kea.enter.enterbe.domain.apply.entity.Apply;
import kea.enter.enterbe.domain.apply.entity.ApplyState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static kea.enter.enterbe.domain.apply.entity.QApply.apply;

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

    // 사용자 아이디(이메일) 검색
    private BooleanExpression idContains(ApplicantSearchType searchType, String keyword) {
        if(searchType == ApplicantSearchType.NAME) return null;
        return keyword != null ? apply.member.email.contains(keyword) : null;
    }

    // 사용자 이름 검색
    private BooleanExpression nameContains(ApplicantSearchType searchType, String keyword) {
        if(searchType == ApplicantSearchType.ID) return null;
        return keyword != null ? apply.member.name.contains(keyword) : null;
    }
}