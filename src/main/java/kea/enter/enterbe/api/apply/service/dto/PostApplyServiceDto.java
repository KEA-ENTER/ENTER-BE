package kea.enter.enterbe.api.apply.service.dto;

import kea.enter.enterbe.domain.apply.entity.ApplyPurpose;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostApplyServiceDto {
    private Long memberId;
    private Long applyRoundId;
    private ApplyPurpose purpose;

    @Builder
    public PostApplyServiceDto(Long memberId, Long applyRoundId, ApplyPurpose purpose){
        this.memberId = memberId;
        this.applyRoundId = applyRoundId;
        this.purpose = purpose;
    }
    public static PostApplyServiceDto of(Long memberId, Long applyRoundId, ApplyPurpose purpose){
        return PostApplyServiceDto.builder()
            .memberId(memberId)
            .applyRoundId(applyRoundId)
            .purpose(purpose)
            .build();
    }

}
