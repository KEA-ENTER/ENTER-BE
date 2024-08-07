package kea.enter.enterbe.api.lottery.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetRecentWaitingAverageNumbersResponse {

    private Integer round;
    private String WaitingAverageNumber;

    @Builder
    public GetRecentWaitingAverageNumbersResponse(Integer round, String WaitingAverageNumber) {
        this.round = round;
        this.WaitingAverageNumber = WaitingAverageNumber;
    }

    public static GetRecentWaitingAverageNumbersResponse of(Integer round, String WaitingAverageNumber) {
        return GetRecentWaitingAverageNumbersResponse
            .builder()
            .round(round)
            .WaitingAverageNumber(WaitingAverageNumber)
            .build();
    }
}
