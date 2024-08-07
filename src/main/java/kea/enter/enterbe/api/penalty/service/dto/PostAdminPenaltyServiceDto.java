package kea.enter.enterbe.api.penalty.service.dto;

import kea.enter.enterbe.domain.penalty.entity.PenaltyLevel;
import kea.enter.enterbe.domain.penalty.entity.PenaltyReason;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostAdminPenaltyServiceDto {
    private Long memberId;

    private PenaltyReason reason;

    private PenaltyLevel level;

    private String etc;

    @Builder
    public PostAdminPenaltyServiceDto(Long memberId, PenaltyReason reason, PenaltyLevel level, String etc) {
        this.memberId = memberId;
        this.reason = reason;
        this.level = level;
        this.etc = etc;
    }

    public static PostAdminPenaltyServiceDto of(Long memberId, PenaltyReason reason, PenaltyLevel level, String etc) {
        return PostAdminPenaltyServiceDto.builder()
            .memberId(memberId)
            .reason(reason)
            .level(level)
            .etc(etc)
            .build();
    }

}
