package kea.enter.enterbe.domain.question.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kea.enter.enterbe.api.question.controller.dto.request.QuestionSearchType;
import kea.enter.enterbe.domain.question.entity.Question;
import kea.enter.enterbe.domain.question.entity.QuestionCategory;
import kea.enter.enterbe.domain.question.entity.QuestionState;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static kea.enter.enterbe.domain.question.entity.QQuestion.question;

@RequiredArgsConstructor
public class CustomQuestionRepositoryImpl implements CustomQuestionRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Question> searchQuestions(String keyword, QuestionSearchType searchType,
        Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if (keyword != null && !keyword.trim().isEmpty()) {
            if (searchType == QuestionSearchType.CATEGORY) {
                // CATEGORY 검색일 경우 프론트에서 전달된 문자열을 Enum으로 매핑
                QuestionCategory category = mapToCategoryEnum(keyword);
                if(category != null){
                    builder.and(question.category.eq(category));
                }
            } else if (searchType == QuestionSearchType.STATE) {
                // STATE 검색일 경우 프론트에서 전달된 문자열을 Enum으로 매핑
                QuestionState state = mapToStateEnum(keyword);
                if(state != null){
                    builder.and(question.state.eq(state));
                }
            } else if (searchType == QuestionSearchType.WRITER) {
                builder.and(question.member.name.contains(keyword));
            } else if (searchType == QuestionSearchType.ALL) {
                // ALL의 경우 이름, 카테고리, 상태 모두를 검색 대상으로 설정
                BooleanBuilder allBuilder = new BooleanBuilder();

                QuestionCategory category = mapToCategoryEnum(keyword);
                QuestionState state = mapToStateEnum(keyword);

                allBuilder.or(question.member.name.contains(keyword));

                if (state != null) {
                    allBuilder.or(question.state.eq(state));
                }
                if (category != null) {
                    allBuilder.or(question.category.eq(category));
                }

                builder.and(allBuilder);
            }
        }

        List<Question> results = queryFactory
            .selectFrom(question)
            .where(builder.and(question.state.ne(QuestionState.INACTIVE)))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(question.id.desc())
            .fetch();

        long size = queryFactory.select(question.count())
            .from(question)
            .where(question.state.ne(QuestionState.INACTIVE))
            .fetchOne();

        return PageableExecutionUtils.getPage(results, pageable, () -> size);
    }

    // 프론트에서 전달된 키워드를 QuestionCategory Enum으로 변환
    private QuestionCategory mapToCategoryEnum(String keyword) {
        // 입력된 키워드가 한글 명칭과 부분적으로 일치하는 경우 전체 명칭을 찾아냄.
        String fullKeyword = null;

        if ("사용자".contains(keyword)) {
            fullKeyword = "사용자";
        } else if ("서비스".contains(keyword)) {
            fullKeyword = "서비스";
        } else if ("차량 문의".contains(keyword)) {
            fullKeyword = "차량 문의";
        } else if ("기타".contains(keyword)) {
            fullKeyword = "기타";
        } else {
            return null;
        }

        // 매핑된 전체 명칭을 Enum으로 변환하여 반환.
        return switch (fullKeyword) {
            case "사용자" -> QuestionCategory.USER;
            case "서비스" -> QuestionCategory.SERVICE;
            case "차량 문의" -> QuestionCategory.VEHICLE;
            case "기타" -> QuestionCategory.ETC;
            default -> throw new CustomException(ResponseCode.INVALID_QUESTION_SEARCH_CATEGORY);
        };
    }

    // 프론트에서 전달된 키워드를 QuestionState Enum으로 변환
    private QuestionState mapToStateEnum(String keyword) {
        // 입력된 키워드가 한글 명칭과 부분적으로 일치하는 경우 전체 명칭을 찾아냄.
        String fullKeyword = null;

        if ("답변 완료".contains(keyword)) {
            fullKeyword = "답변 완료";
        } else if ("대기".contains(keyword)) {
            fullKeyword = "대기";
        } else {
            return null;
        }

        // 매핑된 전체 명칭을 Enum으로 변환하여 반환.
        return switch (fullKeyword) {
            case "답변 완료" -> QuestionState.COMPLETE;
            case "대기" -> QuestionState.WAIT;
            default -> throw new CustomException(ResponseCode.INVALID_QUESTION_SEARCH_STATE);
        };
    }
}
