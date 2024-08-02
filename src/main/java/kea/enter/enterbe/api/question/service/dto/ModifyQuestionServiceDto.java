package kea.enter.enterbe.api.question.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ModifyQuestionServiceDto {
    private Long memberId;
    private Long questionId;

    @Builder
    public ModifyQuestionServiceDto(Long memberId, Long questionId) {
        this.memberId = memberId;
        this.questionId = questionId;
    }

    public static ModifyQuestionServiceDto of(Long memberId, Long questionId) {
        return ModifyQuestionServiceDto.builder()
            .memberId(memberId)
            .questionId(questionId)
            .build();
    }
}
