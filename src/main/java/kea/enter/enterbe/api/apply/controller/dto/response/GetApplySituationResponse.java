package kea.enter.enterbe.api.apply.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetApplySituationResponse {
    @Schema(description = "현재 회차", example = "24")
    private int applyRound;

    @Schema(description = "신청자수", example = "100")
    private int applyCnt;

    @Schema(description = "배정자수", example = "3")
    private int winningCnt;

    @Schema(description = "취소자수", example = "0")
    private int cancelCnt;

    @Builder
    public GetApplySituationResponse(int applyRound, int applyCnt, int winningCnt, int cancelCnt) {
        this.applyRound = applyRound;
        this.applyCnt = applyCnt;
        this.winningCnt = winningCnt;
        this.cancelCnt = cancelCnt;
    }

    public static GetApplySituationResponse of(int applyRound, int applyCnt, int winningCnt, int cancelCnt) {
        return GetApplySituationResponse.builder()
            .applyRound(applyRound)
            .applyCnt(applyCnt)
            .winningCnt(winningCnt)
            .cancelCnt(cancelCnt)
            .build();
    }
}
