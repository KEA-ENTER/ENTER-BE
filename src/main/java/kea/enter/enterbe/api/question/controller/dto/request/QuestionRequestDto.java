package kea.enter.enterbe.api.question.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kea.enter.enterbe.domain.question.entity.QuestionCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuestionRequestDto {

    @NotNull(message = "멤버 아이디를 입력해야 합니다.")
    @Schema(description = "멤버 ID", example = "2")
    private Long memberId;
    @NotBlank(message = "내용을 입력해야 합니다.")
    @Schema(description = "문의사항 내용", example = "추첨 날짜는 언제인가요?")
    private String content;
    @NotNull(message = "카테고리를 입력해야 합니다.")
    @Schema(description = "문의사항 카테고리(USER, SERVICE, VEHICLE, ETC)", example = "USER")
    private QuestionCategory category;

}

