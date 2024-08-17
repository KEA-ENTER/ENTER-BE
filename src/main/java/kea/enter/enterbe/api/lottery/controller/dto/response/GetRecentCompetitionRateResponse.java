package kea.enter.enterbe.api.lottery.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetRecentCompetitionRateResponse {
    @Schema(description = "회차", example = "11")
    private Integer round;
    @Schema(description = "경쟁률 (51 이라고 반환 되면 51:1이라고 생각하시면 됩니다)", example = "51")
    private String competitionRate;

    @Builder
    public GetRecentCompetitionRateResponse(Integer round, String competitionRate) {
        this.round = round;
        this.competitionRate = competitionRate;
    }

    public static GetRecentCompetitionRateResponse of(Integer round, String competitionRate) {
        return GetRecentCompetitionRateResponse
            .builder()
            .round(round)
            .competitionRate(competitionRate)
            .build();
    }
}
