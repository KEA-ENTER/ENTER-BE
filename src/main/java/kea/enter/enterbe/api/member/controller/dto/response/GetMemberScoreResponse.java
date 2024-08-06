package kea.enter.enterbe.api.member.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetMemberScoreResponse {

    private Double scorePercentile;

    @Builder
    public GetMemberScoreResponse(Double scorePercentile) {
        this.scorePercentile = scorePercentile;
    }

    public static GetMemberScoreResponse of(Double scorePercentile) {
        return GetMemberScoreResponse.builder().scorePercentile(scorePercentile).build();
    }
}
