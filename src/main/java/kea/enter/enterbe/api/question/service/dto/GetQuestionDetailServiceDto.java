package kea.enter.enterbe.api.question.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetQuestionDetailServiceDto {
    private Long questionId;
    private Long memberId;

    @Builder
    public GetQuestionDetailServiceDto(Long questionId, Long memberId) {
        this.questionId = questionId;
        this.memberId = memberId;
    }

    public static GetQuestionDetailServiceDto of(Long questionId, Long memberId) {
        return GetQuestionDetailServiceDto.builder()
            .questionId(questionId)
            .memberId(memberId)
            .build();
    }
}
