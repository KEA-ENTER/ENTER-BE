package kea.enter.enterbe.api.apply.service.dto;

import kea.enter.enterbe.domain.apply.entity.ApplyPurpose;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ModifyApplyDetailServiceDto {
    private Long memberId;
    private Long applyId;
    private Long applyRoundId;
    private ApplyPurpose purpose;
    @Builder
    public ModifyApplyDetailServiceDto(Long memberId, Long applyId, Long applyRoundId, ApplyPurpose purpose){
        this.memberId = memberId;
        this.applyId = applyId;
        this.applyRoundId = applyRoundId;
        this.purpose = purpose;
    }

    public static ModifyApplyDetailServiceDto of(Long memberId, Long applyId, Long applyRoundId, ApplyPurpose purpose){
        return ModifyApplyDetailServiceDto.builder()
            .memberId(memberId)
            .applyId(applyId)
            .applyRoundId(applyRoundId)
            .purpose(purpose)
            .build();
    }
}
