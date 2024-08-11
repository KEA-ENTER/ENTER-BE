package kea.enter.enterbe.api.lottery.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetLotteryResultServiceDto {
    private Long memeberId;

    @Builder
    public GetLotteryResultServiceDto(Long memeberId){
            this.memeberId = memeberId;
        }

    public static GetLotteryResultServiceDto of(Long memeberId) {
        return GetLotteryResultServiceDto.builder()
            .memeberId(memeberId)
            .build();
    }
}
