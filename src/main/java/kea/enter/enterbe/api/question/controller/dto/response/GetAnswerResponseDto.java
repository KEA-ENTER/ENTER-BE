package kea.enter.enterbe.api.question.controller.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class GetAnswerResponseDto {

    @NotBlank(message = "답변 내용을 입력해야 합니다.")
    @Schema(description = "답변 내용", example = "안녕하세요. 답변 드립니다.")
    private String content;

    @Builder
    public GetAnswerResponseDto(String content) {
        this.content = content;
    }

    public static GetAnswerResponseDto of(String content) {
        return GetAnswerResponseDto.builder()
            .content(content)
            .build();
    }
}
