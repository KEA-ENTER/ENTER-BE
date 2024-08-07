package kea.enter.enterbe.api.question.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kea.enter.enterbe.api.question.controller.dto.response.GetAnswerResponseDto;
import kea.enter.enterbe.domain.question.entity.QuestionCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CreateQuestionServiceDto {

    @Schema(description = "멤버 ID", example = "2")
    private Long memberId;
    @Schema(description = "문의사항 내용", example = "추첨 날짜는 언제인가요?")
    private String content;
    @Schema(description = "문의사항 카테고리(USER, SERVICE, VEHICLE, ETC)", example = "USER")
    private QuestionCategory category;

    @Builder
    public CreateQuestionServiceDto(Long memberId, String content, QuestionCategory category) {
        this.memberId = memberId;
        this.content = content;
        this.category = category;
    }

    public static CreateQuestionServiceDto of(Long memberId, String content, QuestionCategory category) {
        return CreateQuestionServiceDto.builder()
            .memberId(memberId)
            .content(content)
            .category(category)
            .build();
    }
}
