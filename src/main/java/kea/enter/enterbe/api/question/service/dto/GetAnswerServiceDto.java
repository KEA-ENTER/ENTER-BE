package kea.enter.enterbe.api.question.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetAnswerServiceDto {
    private Long questionId;

    @Builder
    public GetAnswerServiceDto(Long questionId) {
        this.questionId = questionId;
    }

    public static GetAnswerServiceDto of(Long questionId) {
        return GetAnswerServiceDto.builder()
            .questionId(questionId)
            .build();
    }
}
