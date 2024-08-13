package kea.enter.enterbe.api.question.controller.dto.response;

import kea.enter.enterbe.domain.question.entity.QuestionCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GetAnswerResponseDto {

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

    @Builder
    public GetAnswerResponseDto(String name, String questionContent, QuestionCategory category, String questionCreatedAt, String answerContent, String answerCreatedAt) {
        this.name = name;
        this.questionContent = questionContent;
        this.category = category;
        this.questionCreatedAt = questionCreatedAt;
        this.answerContent = answerContent;
        this.answerCreatedAt = answerCreatedAt;
    }

    public static GetAnswerResponseDto of(String name, String questionContent, QuestionCategory category, String questionCreatedAt, String answerContent, String answerCreatedAt) {
        return GetAnswerResponseDto.builder()
            .name(name)
            .questionContent(questionContent)
            .category(category)
            .questionCreatedAt(questionCreatedAt)
            .answerContent(answerContent)
            .answerCreatedAt(answerCreatedAt)
            .build();
    }
}
