package kea.enter.enterbe.api.lottery.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import java.util.List;

@Getter
@NoArgsConstructor
public class GetLotteryListResponse {
    private List<LotteryInfo> lotteryList;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean hasNextPage;

    @Builder
    public GetLotteryListResponse(List<LotteryInfo> lotteryList, int page, int size, long totalElements, int totalPages, boolean hasNextPage) {
        this.lotteryList = lotteryList;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.hasNextPage = hasNextPage;
    }

    public static GetLotteryListResponse of(List<LotteryInfo> lotteryList, int page, int size, long totalElements, int totalPages, boolean hasNextPage) {
        return GetLotteryListResponse.builder()
            .lotteryList(lotteryList)
            .page(page)
            .size(size)
            .totalElements(totalElements)
            .totalPages(totalPages)
            .hasNextPage(hasNextPage)
            .build();
    }
}
