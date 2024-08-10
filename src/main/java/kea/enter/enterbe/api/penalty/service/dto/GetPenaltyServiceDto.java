package kea.enter.enterbe.api.penalty.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetPenaltyServiceDto {
    private Long memberId;
    private Long penaltyId;

    @Builder
    public GetPenaltyServiceDto(Long memberId, Long penaltyId) {
        this.memberId = memberId;
        this.penaltyId = penaltyId;
    }

    public static GetPenaltyServiceDto of(Long memberId, Long penaltyId) {
        return GetPenaltyServiceDto.builder()
            .memberId(memberId)
            .penaltyId(penaltyId)
            .build();
    }
}
