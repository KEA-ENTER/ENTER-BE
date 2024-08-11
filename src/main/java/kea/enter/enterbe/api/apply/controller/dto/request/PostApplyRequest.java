package kea.enter.enterbe.api.apply.controller.dto.request;

import kea.enter.enterbe.domain.apply.entity.ApplyPurpose;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostApplyRequest {
    private Long applyRoundId;
    private ApplyPurpose purpose;

    @Builder
    public PostApplyRequest(Long applyRoundId, ApplyPurpose purpose){
        this.applyRoundId = applyRoundId;
        this.purpose = purpose;
    }
    public static PostApplyRequest of(Long applyRoundId, ApplyPurpose purpose){
        return PostApplyRequest.builder()
            .applyRoundId(applyRoundId)
            .purpose(purpose)
            .build();
    }

}
