package kea.enter.enterbe.api.penalty.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kea.enter.enterbe.api.vehicle.controller.dto.response.GetAdminVehicleListResponse;
import kea.enter.enterbe.domain.penalty.entity.PenaltyReason;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
public class GetPenaltyListResponse {
    private List<PenaltyInfo> penaltyList;

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
    public GetPenaltyListResponse(List<PenaltyInfo> penaltyList, int page, int size, long totalElements, int totalPages, boolean hasNextPage) {
        this.penaltyList = penaltyList;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.hasNextPage = hasNextPage;
    }

    public static GetPenaltyListResponse of(List<PenaltyInfo> penaltyList, int page, int size, long totalElements, int totalPages, boolean hasNextPage) {
        return GetPenaltyListResponse.builder()
            .penaltyList(penaltyList)
            .page(page)
            .size(size)
            .totalElements(totalElements)
            .totalPages(totalPages)
            .hasNextPage(hasNextPage)
            .build();
    }
}
