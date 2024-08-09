package kea.enter.enterbe.api.apply.controller.dto.request;

import kea.enter.enterbe.domain.apply.entity.ApplyPurpose;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ModifyApplyDetailRequest {
    private Long applyRoundId;
    private ApplyPurpose purpose;
    @Builder
    public ModifyApplyDetailRequest(Long applyRoundId, ApplyPurpose purpose){
        this.applyRoundId =applyRoundId;
        this.purpose = purpose;
    }

    public static ModifyApplyDetailRequest of(Long applyRoundId, ApplyPurpose purpose){
        return ModifyApplyDetailRequest.builder()
            .applyRoundId(applyRoundId)
            .purpose(purpose)
            .build();
    }

}
