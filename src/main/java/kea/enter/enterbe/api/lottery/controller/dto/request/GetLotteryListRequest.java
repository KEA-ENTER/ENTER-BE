package kea.enter.enterbe.api.lottery.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import kea.enter.enterbe.api.lottery.service.dto.GetLotteryListServiceDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
public class GetLotteryListRequest {
    @Schema(description = "검색 키워드", example = "G80")
    private String keyword;

    @NotNull
    @Schema(description = "검색 종류 (ALL, ROUND, VEHICLE)", example = "ALL")
    private LotterySearchType searchType;

    @Builder
    public GetLotteryListRequest(String keyword, LotterySearchType searchType) {
        this.keyword = keyword;
        this.searchType = searchType;
    }

    public static GetLotteryListRequest of(String keyword, LotterySearchType searchType) {
        return GetLotteryListRequest.builder()
            .keyword(keyword)
            .searchType(searchType)
            .build();
    }

    public GetLotteryListServiceDto toService(Pageable pageable) {
        return GetLotteryListServiceDto.of(keyword, searchType, pageable);
    }
}
