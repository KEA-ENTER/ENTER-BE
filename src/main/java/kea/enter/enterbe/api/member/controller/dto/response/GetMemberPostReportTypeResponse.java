package kea.enter.enterbe.api.member.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetMemberPostReportTypeResponse {
    @Schema(description = "보고서 종류 반환 (TAKE(인수), RETURN(반납), NONE(아무것도 해당이 안됨))",example = "TAKE")
    private String reportType;

    @Builder
    public GetMemberPostReportTypeResponse(String reportType) {
        this.reportType = reportType;
    }

    public static GetMemberPostReportTypeResponse of(String reportType) {
        return GetMemberPostReportTypeResponse.builder().reportType(reportType).build();
    }
}
