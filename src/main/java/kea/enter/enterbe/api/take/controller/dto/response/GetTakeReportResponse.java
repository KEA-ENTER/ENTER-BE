package kea.enter.enterbe.api.take.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetTakeReportResponse {
    // 사용일자(인수 일자, 반납 일자), 보고 시간, 사용자 이름, 계기판 사진, 차량 전면 사진, 차량 후면 사진, 차량 측면 사진 (왼쪽), 차량 측면 사진 (오른쪽), 특이사항
    @Schema(description = "보고서 아이디", example = "1")
    private Long reportId;

    @Schema(description = "멤버 아이디", example = "1")
    private Long memberId;

    @Schema(description = "인수 날짜", example = "2024-08-03")
    private String takeDate;

    @Schema(description = "반납 날짜", example = "2024-08-05")
    private String returnDate;

    @Schema(description = "보고 시간", example = "2024.07.11 12:22")
    private String reportTime;

    @Schema(description = "사용자 이름", example = "이다현")
    private String memberName;

    @Schema(description = "보고서 사진 목록", example = "")
    private ReportImage reportImageList;

    @Schema(description = "차량 특이사항", example = "최정훈 한지민 열애설")
    private String vehicleNote;

    @Builder
    public GetTakeReportResponse(Long reportId, Long memberId, String takeDate, String returnDate,
        String reportTime, String memberName, ReportImage reportImageList,
        String vehicleNote) {
        this.reportId = reportId;
        this.memberId = memberId;
        this.takeDate = takeDate;
        this.returnDate = returnDate;
        this.reportTime = reportTime;
        this.memberName = memberName;
        this.reportImageList = reportImageList;
        this.vehicleNote = vehicleNote;
    }

    public static GetTakeReportResponse of(Long reportId, Long memberId, String takeDate, String returnDate,
        String reportTime, String memberName, ReportImage reportImageList,
        String vehicleNote) {
        return GetTakeReportResponse.builder()
            .reportId(reportId)
            .memberId(memberId)
            .takeDate(takeDate)
            .returnDate(returnDate)
            .reportTime(reportTime)
            .memberName(memberName)
            .reportImageList(reportImageList)
            .vehicleNote(vehicleNote)
            .build();
    }
}
