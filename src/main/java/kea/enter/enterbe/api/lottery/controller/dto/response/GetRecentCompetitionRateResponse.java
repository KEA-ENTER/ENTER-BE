package kea.enter.enterbe.api.lottery.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetRecentCompetitionRateResponse {

    private Integer round;
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
