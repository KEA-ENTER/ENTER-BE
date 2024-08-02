package kea.enter.enterbe.api.question.service.dto;

import kea.enter.enterbe.api.penalty.service.dto.DeletePenaltyServiceDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteQuestionServiceDto {
    private Long memberId;
    private Long questionId;

    @Builder
    public DeleteQuestionServiceDto(Long memberId, Long questionId) {
        this.memberId = memberId;
        this.questionId = questionId;
    }

    public static DeleteQuestionServiceDto of(Long memberId, Long questionId) {
        return DeleteQuestionServiceDto.builder()
            .memberId(memberId)
            .questionId(questionId)
            .build();
    }
}
