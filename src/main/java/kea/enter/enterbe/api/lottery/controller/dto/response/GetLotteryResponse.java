package kea.enter.enterbe.api.lottery.controller.dto.response;
import io.swagger.v3.oas.annotations.media.Schema;
import kea.enter.enterbe.api.penalty.controller.dto.response.GetPenaltyListResponse;
import kea.enter.enterbe.api.penalty.controller.dto.response.PenaltyInfo;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
public class GetLotteryResponse {
    private List<LotteryListInfo> lotteryListInfos;

    @Schema(description = "현재 페이지", example = "0")
    private int page;

    @Schema(description = "현재 페이지 사이즈", example = "8")
    private int size;

    @Schema(description = "전체 개수", example = "20")
    private long totalElements;

    @Schema(description = "전체 페이지", example = "3")
    private int totalPages;

    @Schema(description = "다음 페이지 존재 여부", example = "true")
    private boolean hasNextPage;


    @Builder
    public GetLotteryResponse(List<LotteryListInfo> lotteryListInfos, int page, int size, long totalElements, int totalPages, boolean hasNextPage) {
        this.lotteryListInfos = lotteryListInfos;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.hasNextPage = hasNextPage;
    }

    public static GetLotteryResponse of(List<LotteryListInfo> lotteryListInfos, int page, int size, long totalElements, int totalPages, boolean hasNextPage) {
        return GetLotteryResponse.builder()
            .lotteryListInfos(lotteryListInfos)
            .page(page)
            .size(size)
            .totalElements(totalElements)
            .totalPages(totalPages)
            .hasNextPage(hasNextPage)
            .build();
    }
}
