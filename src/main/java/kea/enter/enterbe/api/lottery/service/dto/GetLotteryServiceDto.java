package kea.enter.enterbe.api.lottery.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetLotteryServiceDto {
    private Long memberId;

    @Builder
    public GetLotteryServiceDto(Long memberId) {
        this.memberId = memberId;
    }

    public static GetLotteryServiceDto of(Long memberId) {
        return GetLotteryServiceDto.builder()
            .memberId(memberId)
            .build();
    }
}
