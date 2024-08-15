package kea.enter.enterbe.api.question.controller.dto.response;

import kea.enter.enterbe.domain.question.entity.QuestionCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@NoArgsConstructor
public class GetQuestionDetailResponseDto {

    @Schema(description = "문의사항 작성자 이름", example = "홍길동")
    private String name;
    @Schema(description = "문의사항 내용", example = "안녕하세요. 질문 드립니다.")
    private String questionContent;
    @Schema(description = "문의사항 카테고리", example = "SERVICE")
    private QuestionCategory category;
    @Schema(description = "문의사항 작성 날짜", example = "2023-01-01 00:00:00")
    private String questionCreatedAt;
    @Schema(description = "답변 내용", example = "안녕하세요. 답변 드립니다.")
    private String answerContent;
    @Schema(description = "답변 작성 날짜", example = "2023-01-01 00:00:00")
    private String answerCreatedAt;
    @Schema(description = "본인의 글인지 확인", example = "true")
    private boolean myQuestion;

    @Builder
    public GetQuestionDetailResponseDto(String name, String questionContent, QuestionCategory category, String questionCreatedAt, String answerContent, String answerCreatedAt, boolean myQuestion) {
        this.name = name;
        this.questionContent = questionContent;
        this.category = category;
        this.questionCreatedAt = questionCreatedAt;
        this.answerContent = answerContent;
        this.answerCreatedAt = answerCreatedAt;
        this.myQuestion = myQuestion;
    }

    public static GetQuestionDetailResponseDto of(String name, String questionContent, QuestionCategory category, String questionCreatedAt, String answerContent, String answerCreatedAt, boolean myQuestion) {
        return GetQuestionDetailResponseDto.builder()
            .name(name)
            .questionContent(questionContent)
            .category(category)
            .questionCreatedAt(questionCreatedAt)
            .answerContent(answerContent)
            .answerCreatedAt(answerCreatedAt)
            .myQuestion(myQuestion)
            .build();
    }
}
