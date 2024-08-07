package kea.enter.enterbe.api.lottery.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import kea.enter.enterbe.api.lottery.service.dto.GetApplicantListServiceDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
public class GetApplicantListRequest {
    @Schema(description = "검색 키워드", example = "test11")
    private String keyword;

    @NotNull
    @Schema(description = "검색 종류 (ALL, ID, NAME)", example = "ALL")
    private ApplicantSearchType searchType;

    @Builder
    public GetApplicantListRequest(String keyword, ApplicantSearchType searchType) {
        this.keyword = keyword;
        this.searchType = searchType;
    }

    public static GetApplicantListRequest of(String keyword, ApplicantSearchType searchType) {
        return GetApplicantListRequest.builder()
            .keyword(keyword)
            .searchType(searchType)
            .build();
    }

    public GetApplicantListServiceDto toService(Long applyRoundId, Pageable pageable) {
        return GetApplicantListServiceDto.of(applyRoundId, keyword, searchType, pageable);
    }
}