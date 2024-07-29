package kea.enter.enterbe.api.controller.question.dto.request;

import kea.enter.enterbe.domain.question.entity.QuestionCategory;
import kea.enter.enterbe.domain.question.entity.QuestionState;
import lombok.Data;

@Data
public class QuestionRequestDto {
    private Long memberId;
    private String content;
    private QuestionCategory category;
}

