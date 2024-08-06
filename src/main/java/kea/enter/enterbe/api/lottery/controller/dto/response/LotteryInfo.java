package kea.enter.enterbe.api.lottery.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LotteryInfo {
    @Schema(description = "신청 회차 아이디", example = "1")
    private Long applyRoundId;

    @Schema(description = "회차", example = "24")
    private int applyRound;

    @Schema(description = "인수 날짜", example = "2024-08-03")
    private String takeDate;

    @Schema(description = "반납 날짜", example = "2024-08-05")
    private String returnDate;

    @Schema(description = "차량 모델", example = "G80")
    private String vehicleModel;

    @Schema(description = "차량 번호", example = "123가 4568")
    private String vehicleNo;

    @Schema(description = "신청자수", example = "100")
    private int applyCnt;

    @Schema(description = "당첨자수", example = "3")
    private int winningCnt;

    @Schema(description = "미인수자수", example = "0")
    private int noShowCnt;

    @Schema(description = "경쟁률", example = "100:1")
    private String competition ;

    @Builder
    public LotteryInfo(Long applyRoundId, int applyRound, String takeDate, String returnDate, String vehicleModel, String vehicleNo, int applyCnt, int winningCnt, int noShowCnt, String competition) {
        this.applyRoundId = applyRoundId;
        this.applyRound = applyRound;
        this.takeDate = takeDate;
        this.returnDate = returnDate;
        this.vehicleModel = vehicleModel;
        this.vehicleNo = vehicleNo;
        this.applyCnt = applyCnt;
        this.winningCnt = winningCnt;
        this.noShowCnt = noShowCnt;
        this.competition = competition;
    }

    public static LotteryInfo of(Long applyRoundId, int applyRound, String takeDate, String returnDate,  String vehicleModel, String vehicleNo, int applyCnt, int winningCnt, int noShowCnt, String competition) {
        return LotteryInfo.builder()
            .applyRoundId(applyRoundId)
            .applyRound(applyRound)
            .takeDate(takeDate)
            .returnDate(returnDate)
            .vehicleModel(vehicleModel)
            .vehicleNo(vehicleNo)
            .applyCnt(applyCnt)
            .winningCnt(winningCnt)
            .noShowCnt(noShowCnt)
            .competition(competition)
            .build();
    }
}
