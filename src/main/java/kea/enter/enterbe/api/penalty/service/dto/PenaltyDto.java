package kea.enter.enterbe.api.penalty.service.dto;

import kea.enter.enterbe.domain.penalty.entity.PenaltyReason;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PenaltyDto {
    private PenaltyReason reason;

    private String etc;

    @Builder
    public PenaltyDto(PenaltyReason reason, String etc) {
        this.reason = reason;
        this.etc = etc;
    }

    public static PenaltyDto of(PenaltyReason reason, String etc) {
        return PenaltyDto.builder()
            .reason(reason)
            .etc(etc)
            .build();
    }

}
