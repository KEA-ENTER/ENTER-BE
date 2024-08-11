package kea.enter.enterbe.api.vehicle.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import kea.enter.enterbe.api.lottery.service.dto.GetLotteryListServiceDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetAdminVehicleListRequest {
    @Schema(description = "검색어", example = "그랜저")
    private String word;

    @Schema(description = "검색 카테고리", example = "VEHICLENO")
    @NotBlank(message = "검색 카테고리를 입력해야 합니다. (ALL, VEHICLENO, MODEL, STATE)")
    private VehicleSearchCategory searchCategory;


    @Builder
    public GetAdminVehicleListRequest(String word, VehicleSearchCategory searchCategory) {
        this.word = word;
        this.searchCategory = searchCategory;
    }

    public static GetAdminVehicleListRequest of(String word, VehicleSearchCategory searchCategory) {
        return GetAdminVehicleListRequest.builder()
            .word(word)
            .searchCategory(searchCategory)
            .build();
    }
}
