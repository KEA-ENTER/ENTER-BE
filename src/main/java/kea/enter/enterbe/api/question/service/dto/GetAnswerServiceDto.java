package kea.enter.enterbe.api.question.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetAnswerServiceDto {
    private Long questionId;
    private Long memberId;

    @Builder
    public GetAnswerServiceDto(Long questionId, Long memberId) {
        this.questionId = questionId;
        this.memberId = memberId;
    }

    public static GetAnswerServiceDto of(Long questionId, Long memberId) {
        return GetAnswerServiceDto.builder()
            .questionId(questionId)
            .memberId(memberId)
            .build();
    }
}
