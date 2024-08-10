package kea.enter.enterbe.api.lottery.controller.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;


@Getter
@NoArgsConstructor
public class GetLotteryResponse {
    @Schema(description = "당첨 여부", example = "true")
    private boolean winning;
    @Schema(description = "대기 번호", example = "5")
    private Integer waitingNumber;

    @Builder
    public GetLotteryResponse(boolean winning, Integer waitingNumber){
        this.winning = winning;
        this.waitingNumber = waitingNumber;
    }

    public static GetLotteryResponse of(boolean winning, Integer waitingNumber){
        return GetLotteryResponse.builder()
            .winning(winning)
            .waitingNumber(waitingNumber)
            .build();
    }

}
