package kea.enter.enterbe.api.question.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AnswerRequestDto {

    @NotNull(message = "답변 작성자 아이디를 입력해야 합니다.")
    @Schema(description = "답변 작성자 ID", example = "2")
    private Long memberId;

    @NotBlank(message = "답변 내용을 입력해야 합니다.")
    @Schema(description = "답변 내용", example = "안녕하세요. 답변 드립니다.")
    private String content;

    @Builder
    public AnswerRequestDto(Long memberId, String content) {
        this.memberId = memberId;
        this.content = content;
    }

    public static AnswerRequestDto of(Long memberId, String content) {
        return AnswerRequestDto.builder()
            .memberId(memberId)
            .content(content)
            .build();
    }

}
