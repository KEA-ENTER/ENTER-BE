package kea.enter.enterbe.api.lottery.controller.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetLotteryResponse {
    private boolean winning;
    private int waitingNumber;

    @Builder
    public GetLotteryResponse(boolean winning, int waitingNumber){
        this.winning = winning;
        this.waitingNumber = waitingNumber;
    }

    public static GetLotteryResponse of(boolean winning, int waitingNumber){
        return GetLotteryResponse.builder()
            .winning(winning)
            .waitingNumber(waitingNumber)
            .build();
    }

}
