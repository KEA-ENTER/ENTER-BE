package kea.enter.enterbe.api.question.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AnswerRequestDto {

    @NotBlank(message = "답변 내용을 입력해야 합니다.")
    @Schema(description = "답변 내용", example = "안녕하세요. 답변 드립니다.")
    private String content;

    @Builder
    public AnswerRequestDto(String content) {
        this.content = content;
    }

    public static AnswerRequestDto of(String content) {
        return AnswerRequestDto.builder()
            .content(content)
            .build();
    }

}
