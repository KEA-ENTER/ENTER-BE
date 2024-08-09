package kea.enter.enterbe.api.apply.controller.dto.request;

import kea.enter.enterbe.domain.apply.entity.ApplyPurpose;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ModifyApplyDetailRequest {
    private Long applyId;
    private Long applyRoundId;
    private ApplyPurpose purpose;
    @Builder
    public ModifyApplyDetailRequest(Long applyId, Long applyRoundId, ApplyPurpose purpose){
        this.applyId = applyId;
        this.applyRoundId =applyRoundId;
        this.purpose = purpose;
    }

    public static ModifyApplyDetailRequest of(Long applyId, Long applyRoundId, ApplyPurpose purpose){
        return ModifyApplyDetailRequest.builder()
            .applyId(applyId)
            .applyRoundId(applyRoundId)
            .purpose(purpose)
            .build();
    }

}
