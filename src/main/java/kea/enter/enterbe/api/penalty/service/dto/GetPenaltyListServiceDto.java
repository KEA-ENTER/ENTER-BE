package kea.enter.enterbe.api.penalty.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetPenaltyListServiceDto {
    private Long memberId;

    @Builder
    public GetPenaltyListServiceDto(Long memberId) {
        this.memberId = memberId;
    }

    public static GetPenaltyListServiceDto of(Long memberId) {
        return GetPenaltyListServiceDto.builder()
            .memberId(memberId)
            .build();
    }
}
