package kea.enter.enterbe.api.take.service.dto;

import kea.enter.enterbe.api.take.controller.dto.request.ReportSearchType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

@Getter
@NoArgsConstructor
public class GetReportListServiceDto {
    private String keyword;

    private ReportSearchType searchType;

    private Pageable pageable;

    @Builder
    public GetReportListServiceDto(String keyword, ReportSearchType searchType, Pageable pageable) {
        this.keyword = keyword;
        this.searchType = searchType;
        this.pageable = pageable;
    }

    public static GetReportListServiceDto of(String keyword, ReportSearchType searchType, Pageable pageable) {
        return GetReportListServiceDto.builder()
            .keyword(keyword)
            .searchType(searchType)
            .pageable(pageable)
            .build();
    }
}
