package kea.enter.enterbe.api.member.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetMemberScoreResponse {
    @Schema(description = "가중치 상위 몇 퍼센트인지 확인 ", example = "41.39281839")
    private Double scorePercentile;

    @Builder
    public GetMemberScoreResponse(Double scorePercentile) {
        this.scorePercentile = scorePercentile;
    }

    public static GetMemberScoreResponse of(Double scorePercentile) {
        return GetMemberScoreResponse.builder().scorePercentile(scorePercentile).build();
    }
}
