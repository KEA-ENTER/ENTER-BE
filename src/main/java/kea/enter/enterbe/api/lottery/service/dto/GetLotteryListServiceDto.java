package kea.enter.enterbe.api.lottery.service.dto;

import kea.enter.enterbe.api.lottery.controller.dto.request.LotterySearchType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

@Getter
@NoArgsConstructor
public class GetLotteryListServiceDto {
    private String keyword;

    private LotterySearchType searchType;

    private Pageable pageable;

    @Builder
    public GetLotteryListServiceDto(String keyword, LotterySearchType searchType, Pageable pageable) {
        this.keyword = keyword;
        this.searchType = searchType;
        this.pageable = pageable;
    }

    public static GetLotteryListServiceDto of(String keyword, LotterySearchType searchType, Pageable pageable) {
        return GetLotteryListServiceDto.builder()
            .keyword(keyword)
            .searchType(searchType)
            .pageable(pageable)
            .build();
    }
}
