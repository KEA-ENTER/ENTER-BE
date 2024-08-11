package kea.enter.enterbe.api.vehicle.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetAdminVehicleListResponse {

    private List<VehicleInfo> vehicleList;

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
    public GetAdminVehicleListResponse(List<VehicleInfo> vehicleList, int page, int size, long totalElements, int totalPages, boolean hasNextPage) {
        this.vehicleList = vehicleList;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.hasNextPage = hasNextPage;
    }

    public static GetAdminVehicleListResponse of(List<VehicleInfo> vehicleList, int page, int size, long totalElements, int totalPages, boolean hasNextPage) {
        return GetAdminVehicleListResponse.builder()
            .vehicleList(vehicleList)
            .page(page)
            .size(size)
            .totalElements(totalElements)
            .totalPages(totalPages)
            .hasNextPage(hasNextPage)
            .build();
    }
}
