package kea.enter.enterbe.api.take.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReportInfo {
    @Schema(description = "당첨 아이디", example = "1")
    private Long winningId;

    @Schema(description = "차량 모델", example = "G80")
    private String vehicleModel;

    @Schema(description = "차량 번호", example = "123가 4568")
    private String vehicleNo;

    @Schema(description = "인수 날짜", example = "2024-08-03")
    private String takeDate;

    @Schema(description = "반납 날짜", example = "2024-08-05")
    private String returnDate;

    @Schema(description = "인수자명", example = "이다현")
    private String memberName ;

    @Schema(description = "상태 (BEFORE_TAKE, TAKE, RETURN)", example = "이다현")
    private ReportState state ;

    @Schema(description = "인수 보고서 존재 여부 (true, false)", example = "true")
    private Boolean hasTakeReport;

    @Schema(description = "반납 보고서 존재 여부 (true, false)", example = "true")
    private Boolean hasReturnReport;

    @Builder
    public ReportInfo(Long winningId, String vehicleModel, String vehicleNo,
        String takeDate, String returnDate, String memberName, ReportState state, Boolean hasTakeReport, Boolean hasReturnReport) {
        this.winningId = winningId;
        this.vehicleModel = vehicleModel;
        this.vehicleNo = vehicleNo;
        this.takeDate = takeDate;
        this.returnDate = returnDate;
        this.memberName = memberName;
        this.state = state;
        this.hasTakeReport = hasTakeReport;
        this.hasReturnReport = hasReturnReport;
    }

    public static ReportInfo of(Long winningId, String vehicleModel, String vehicleNo,
        String takeDate, String returnDate, String memberName, ReportState state, Boolean hasTakeReport, Boolean hasReturnReport) {
        return ReportInfo.builder()
            .winningId(winningId)
            .vehicleModel(vehicleModel)
            .vehicleNo(vehicleNo)
            .takeDate(takeDate)
            .returnDate(returnDate)
            .memberName(memberName)
            .state(state)
            .hasTakeReport(hasTakeReport)
            .hasReturnReport(hasReturnReport)
            .build();
    }
}
