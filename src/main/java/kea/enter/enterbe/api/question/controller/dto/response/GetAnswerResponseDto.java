package kea.enter.enterbe.api.question.controller.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GetAnswerResponseDto {

    @NotBlank(message = "답변 내용을 입력해야 합니다.")
    @Schema(description = "답변 내용", example = "안녕하세요. 답변 드립니다.")
    private String content;
    @NotNull
    @Schema(description = "작성 날짜", example = "2023-01-01 00:00:00")
    private LocalDateTime createdAt;
    @NotNull
    @Schema(description = "멤버 역할", example = "ADMIN")
    private String memberRole;

    @Builder
    public GetAnswerResponseDto(String content, LocalDateTime createdAt, String memberRole) {
        this.content = content;
        this.createdAt = createdAt;
        this.memberRole = memberRole;
    }

    public static GetAnswerResponseDto of(String content, LocalDateTime createdAt, String memberRole) {
        return GetAnswerResponseDto.builder()
            .content(content)
            .createdAt(createdAt)
            .memberRole(memberRole)
            .build();
    }
}
