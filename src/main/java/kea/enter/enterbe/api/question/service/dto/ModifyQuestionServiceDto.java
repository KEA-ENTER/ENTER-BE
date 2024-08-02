package kea.enter.enterbe.api.question.service.dto;

import kea.enter.enterbe.domain.question.entity.QuestionCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ModifyQuestionServiceDto {

    private Long memberId;
    private Long questionId;
    private String content;
    private QuestionCategory category;

    @Builder
    public ModifyQuestionServiceDto(Long memberId, Long questionId, String content,
        QuestionCategory category) {
        this.memberId = memberId;
        this.questionId = questionId;
        this.content = content;
        this.category = category;
    }

    public static ModifyQuestionServiceDto of(Long memberId, Long questionId, String content,
        QuestionCategory category) {
        return ModifyQuestionServiceDto.builder()
            .memberId(memberId)
            .questionId(questionId)
            .content(content)
            .category(category)
            .build();
    }
}
