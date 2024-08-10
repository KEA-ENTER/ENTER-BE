package kea.enter.enterbe.api.question.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kea.enter.enterbe.domain.question.entity.QuestionCategory;
import kea.enter.enterbe.domain.question.entity.QuestionState;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
public class GetQuestionListResponseDto {

    @Schema(description = "현재 페이지의 문의사항 목록")
    private List<QuestionDetailDto> questions;

    @Schema(description = "전체 페이지 수", example = "10")
    private int totalPages;

    @Schema(description = "다음 페이지 여부", example = "false")
    private boolean hasNextPage;

    @Builder
    public GetQuestionListResponseDto(List<QuestionDetailDto> questions, int totalPages,
        boolean hasNextPage) {
        this.questions = questions;
        this.totalPages = totalPages;
        this.hasNextPage = hasNextPage;
    }

    public static GetQuestionListResponseDto of(List<QuestionDetailDto> questions, int totalPages,
        boolean hasNextPage) {
        return GetQuestionListResponseDto.builder()
            .questions(questions)
            .totalPages(totalPages)
            .hasNextPage(hasNextPage)
            .build();
    }

    @Getter
    @NoArgsConstructor
    public static class QuestionDetailDto {

        @Schema(description = "문의사항 ID", example = "1")
        private Long questionId;

        @Schema(description = "문의사항 작성자 이름", example = "홍길동")
        private String name;

        @Schema(description = "문의사항 내용", example = "안녕하세요. 질문 드립니다.")
        private String questionContent;

        @Schema(description = "문의사항 카테고리", example = "SERVICE")
        private QuestionCategory category;

        @Schema(description = "문의사항 작성 날짜", example = "2023-01-01 00:00:00")
        private String questionCreatedAt;

        @Schema(description = "문의사항 처리 상태", example = "COMPLETE")
        private QuestionState state;

        @Builder
        public QuestionDetailDto(Long questionId, String name, String questionContent,
            QuestionCategory category, String questionCreatedAt, QuestionState state) {
            this.questionId = questionId;
            this.name = name;
            this.questionContent = questionContent;
            this.category = category;
            this.questionCreatedAt = questionCreatedAt;
            this.state = state;
        }

        public static QuestionDetailDto of(Long questionId, String name, String questionContent,
            QuestionCategory category, String questionCreatedAt, QuestionState state) {
            return QuestionDetailDto.builder()
                .questionId(questionId)
                .name(name)
                .questionContent(questionContent)
                .category(category)
                .questionCreatedAt(questionCreatedAt)
                .state(state)
                .build();
        }
    }
}
