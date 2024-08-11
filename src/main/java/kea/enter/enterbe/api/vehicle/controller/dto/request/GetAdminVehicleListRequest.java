package kea.enter.enterbe.api.vehicle.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetAdminVehicleListRequest {
    @Schema(description = "검색어", example = "그랜저")
    private String word;

    @Schema(description = "검색 카테고리 (ALL, VEHICLENO, MODEL, STATE)", example = "VEHICLENO")
    //@NotNull(message = "검색 카테고리를 입력해야 합니다.")
    private String searchCategory;


    @Builder
    public GetAdminVehicleListRequest(String word, String searchCategory) {
        this.word = word;
        this.searchCategory = searchCategory;
    }

    public static GetAdminVehicleListRequest of(String word, String searchCategory) {
        return GetAdminVehicleListRequest.builder()
            .word(word)
            .searchCategory(searchCategory)
            .build();
    }
}
