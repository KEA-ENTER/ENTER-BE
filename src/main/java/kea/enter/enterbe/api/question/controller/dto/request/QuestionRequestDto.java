package kea.enter.enterbe.api.question.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kea.enter.enterbe.domain.question.entity.QuestionCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class QuestionRequestDto {

    @NotNull(message = "멤버 아이디를 입력해야 합니다.")
    private Long memberId;
    @NotBlank(message = "내용을 입력해야 합니다.")
    private String content;
    @NotNull(message = "카테고리를 입력해야 합니다.")
    private QuestionCategory category;

}

