package kea.enter.enterbe.api.question.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetQuestionListRequestDto {
    @NotNull(message = "페이지 번호를 넘겨주어야 합니다.")
    @Schema(description = "페이지 번호", example = "1")
    private int pageNumber;

    @Builder
    public GetQuestionListRequestDto(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public static GetQuestionListRequestDto of(int pageNumber) {
        return GetQuestionListRequestDto.builder()
            .pageNumber(pageNumber)
            .build();
    }
}
