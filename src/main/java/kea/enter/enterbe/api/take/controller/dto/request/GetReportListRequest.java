package kea.enter.enterbe.api.take.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import kea.enter.enterbe.api.take.service.dto.GetReportListServiceDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
public class GetReportListRequest {
    @Schema(description = "검색 키워드", example = "G80")
    private String keyword;

    @NotNull
    @Schema(description = "검색 종류 (ALL, STATE, MEMBER, VEHICLE)", example = "ALL")
    private ReportSearchType searchType;

    @Builder
    public GetReportListRequest(String keyword, ReportSearchType searchType) {
        this.keyword = keyword;
        this.searchType = searchType;
    }

    public static GetReportListRequest of(String keyword, ReportSearchType searchType) {
        return GetReportListRequest.builder()
            .keyword(keyword)
            .searchType(searchType)
            .build();
    }

    public GetReportListServiceDto toService(Pageable pageable) {
        return GetReportListServiceDto.of(keyword, searchType, pageable);
    }
}
