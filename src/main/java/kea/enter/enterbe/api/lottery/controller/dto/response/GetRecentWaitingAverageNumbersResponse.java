package kea.enter.enterbe.api.lottery.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetRecentWaitingAverageNumbersResponse {
    @Schema(description = "회차", example = "11")
    private Integer round;

    @Schema(description = "대기번호 빠짐 수(0이면 처음 당첨된 사람이 가져간것, 나머지는 1,2,3...)", example = "14")
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
