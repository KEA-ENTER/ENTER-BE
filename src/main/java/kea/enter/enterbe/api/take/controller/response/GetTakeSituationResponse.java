package kea.enter.enterbe.api.take.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetTakeSituationResponse {
    @Schema(description = "이전 회차", example = "24")
    private int applyRound;

    @Schema(description = "신청자수", example = "100")
    private int applyCnt;

    @Schema(description = "인수자수", example = "3")
    private int takeCnt;

    @Schema(description = "미인수자수", example = "0")
    private int noShowCnt;

    @Builder
    public GetTakeSituationResponse(int applyRound, int applyCnt, int takeCnt, int noShowCnt) {
        this.applyRound = applyRound;
        this.applyCnt = applyCnt;
        this.takeCnt = takeCnt;
        this.noShowCnt = noShowCnt;
    }

    public static GetTakeSituationResponse of(int applyRound, int applyCnt, int takeCnt, int noShowCnt) {
        return GetTakeSituationResponse.builder()
            .applyRound(applyRound)
            .applyCnt(applyCnt)
            .takeCnt(takeCnt)
            .noShowCnt(noShowCnt)
            .build();
    }
}
