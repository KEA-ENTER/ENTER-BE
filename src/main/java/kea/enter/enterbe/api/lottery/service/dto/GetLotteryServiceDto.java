package kea.enter.enterbe.api.lottery.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetLotteryServiceDto {
    private Long memeberId;

    @Builder
    public GetLotteryServiceDto(Long memeberId){
        this.memeberId = memeberId;
    }

    public static GetLotteryServiceDto of(Long memeberId){
        return GetLotteryServiceDto.builder()
            .memeberId(memeberId)
            .build();
    }
}
