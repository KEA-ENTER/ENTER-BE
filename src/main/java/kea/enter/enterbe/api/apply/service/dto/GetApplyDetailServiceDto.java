package kea.enter.enterbe.api.apply.service.dto;

import kea.enter.enterbe.api.apply.controller.dto.response.GetApplyDetailResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetApplyDetailServiceDto {
    private long memberId;

    @Builder
    public GetApplyDetailServiceDto(long memberId){
        this.memberId = memberId;
    }

    public static GetApplyDetailServiceDto of(long memberId){
        return GetApplyDetailServiceDto.builder()
            .memberId(memberId)
            .build();
    }

}
