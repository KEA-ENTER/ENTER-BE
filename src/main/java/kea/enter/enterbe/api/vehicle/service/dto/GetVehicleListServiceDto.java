package kea.enter.enterbe.api.vehicle.service.dto;

import kea.enter.enterbe.api.vehicle.controller.dto.request.VehicleSearchCategory;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
public class GetVehicleListServiceDto {
    private String word;
    private VehicleSearchCategory searchCategory;
    private Pageable pageable;

    @Builder
    public GetVehicleListServiceDto(String word, VehicleSearchCategory searchCategory, Pageable pageable) {
        this.word = word;
        this.searchCategory = searchCategory;
        this.pageable = pageable;
    }

    public static GetVehicleListServiceDto of(String word, VehicleSearchCategory searchCategory, Pageable pageable) {
        return GetVehicleListServiceDto.builder()
            .word(word)
            .searchCategory(searchCategory)
            .pageable(pageable)
            .build();
    }
}
