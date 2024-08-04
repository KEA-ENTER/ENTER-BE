package kea.enter.enterbe.api.question.controller.dto.request;

import kea.enter.enterbe.api.penalty.service.dto.GetPenaltyListServiceDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetAnswerRequestDto {
    private Long memberId;

    @Builder
    public GetAnswerRequestDto(Long memberId) {
        this.memberId = memberId;
    }

    public static GetAnswerRequestDto of(Long memberId) {
        return GetAnswerRequestDto.builder()
            .memberId(memberId)
            .build();
    }
}
