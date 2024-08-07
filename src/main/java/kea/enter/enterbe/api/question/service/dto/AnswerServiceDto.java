package kea.enter.enterbe.api.question.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AnswerServiceDto {
    @Schema(description = "답변 작성자 ID", example = "2")
    private Long memberId;

    @Schema(description = "답변 내용", example = "안녕하세요. 답변 드립니다.")
    private String content;

    @Schema(description = "문의사항 ID", example = "1")
    private Long questionId;

    @Builder
    public AnswerServiceDto(Long memberId, String content, Long questionId) {
        this.memberId = memberId;
        this.content = content;
        this.questionId = questionId;
    }

    public static AnswerServiceDto of(Long memberId, String content, Long questionId) {
        return AnswerServiceDto.builder()
            .memberId(memberId)
            .content(content)
            .questionId(questionId)
            .build();
    }
}
