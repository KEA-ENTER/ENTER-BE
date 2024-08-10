package kea.enter.enterbe.api.lottery.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetLotteryResponse {
    @Schema(description = "회차", example = "1")
    private Integer round;

    @Schema(description = "차량 인수 날짜", example = "08-07")
    private String takeDate;

    @Schema(description = "차량 반납 날짜", example = "08-08")
    private String returnDate;

    @Schema(description = "경쟁률", example = "21:1")
    private long competitionRate;

    @Schema(description = "결과", example = "당첨")
    private String result;

    @Builder
    public GetLotteryResponse(Integer round, String takeDate, String returnDate, long competitionRate, String result) {
        this.round = round;
        this.takeDate = takeDate;
        this.returnDate = returnDate;
        this.competitionRate = competitionRate;
        this.result = result;
    }

    public static GetLotteryResponse of(Integer round, String takeDate, String returnDate, long competitionRate, String result) {
        return GetLotteryResponse.builder()
            .round(round)
            .takeDate(takeDate)
            .returnDate(returnDate)
            .competitionRate(competitionRate)
            .result(result)
            .build();
    }
}
