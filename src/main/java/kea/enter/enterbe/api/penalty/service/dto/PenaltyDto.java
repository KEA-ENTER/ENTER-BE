package kea.enter.enterbe.api.penalty.service.dto;

import kea.enter.enterbe.domain.penalty.entity.PenaltyLevel;
import kea.enter.enterbe.domain.penalty.entity.PenaltyReason;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PenaltyDto {
    private PenaltyReason reason;

    private PenaltyLevel level;

    private String etc;

    @Builder
    public PenaltyDto(PenaltyReason reason, PenaltyLevel level, String etc) {
        this.reason = reason;
        this.level = level;
        this.etc = etc;
    }

    public static PenaltyDto of(PenaltyReason reason, PenaltyLevel level, String etc) {
        return PenaltyDto.builder()
            .reason(reason)
            .level(level)
            .etc(etc)
            .build();
    }

}
