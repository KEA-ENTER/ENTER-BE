package kea.enter.enterbe.api.member.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetMemberPostReportTypeResponse {

    private String reportType;

    @Builder
    public GetMemberPostReportTypeResponse(String reportType) {
        this.reportType = reportType;
    }

    public static GetMemberPostReportTypeResponse of(String reportType) {
        return GetMemberPostReportTypeResponse.builder().reportType(reportType).build();
    }
}
