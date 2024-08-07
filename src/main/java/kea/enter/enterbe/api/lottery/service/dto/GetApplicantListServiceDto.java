package kea.enter.enterbe.api.lottery.service.dto;

import kea.enter.enterbe.api.lottery.controller.dto.request.ApplicantSearchType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

@Getter
@NoArgsConstructor
public class GetApplicantListServiceDto {
    private Long applyRoundId;

    private String keyword;

    private ApplicantSearchType searchType;

    private Pageable pageable;

    @Builder
    public GetApplicantListServiceDto(Long applyRoundId, String keyword, ApplicantSearchType searchType, Pageable pageable) {
        this.applyRoundId = applyRoundId;
        this.keyword = keyword;
        this.searchType = searchType;
        this.pageable = pageable;
    }

    public static GetApplicantListServiceDto of(Long applyRoundId, String keyword, ApplicantSearchType searchType, Pageable pageable) {
        return GetApplicantListServiceDto.builder()
            .applyRoundId(applyRoundId)
            .keyword(keyword)
            .searchType(searchType)
            .pageable(pageable)
            .build();
    }
}