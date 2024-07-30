package kea.enter.enterbe.api.controller.question.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kea.enter.enterbe.domain.question.entity.QuestionCategory;
import lombok.Data;

@Data
public class QuestionRequestDto {
    @NotNull(message = "멤버 아이디를 입력해야 합니다.")
    private Long memberId;
    @NotBlank(message = "내용을 입력해야 합니다.")
    private String content;
    @NotNull(message = "카테고리를 입력해야 합니다.")
    private QuestionCategory category;
}

