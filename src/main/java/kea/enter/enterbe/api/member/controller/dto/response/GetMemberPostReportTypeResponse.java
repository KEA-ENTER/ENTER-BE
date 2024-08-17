package kea.enter.enterbe.api.member.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetMemberPostReportTypeResponse {
    @Schema(description = "보고서 종류 반환 (TAKE, RETURN. NONE)",example = "TAKE")
    private String reportType;

    @Builder
    public GetMemberPostReportTypeResponse(String reportType) {
        this.reportType = reportType;
    }

    public static GetMemberPostReportTypeResponse of(String reportType) {
        return GetMemberPostReportTypeResponse.builder().reportType(reportType).build();
    }
}
