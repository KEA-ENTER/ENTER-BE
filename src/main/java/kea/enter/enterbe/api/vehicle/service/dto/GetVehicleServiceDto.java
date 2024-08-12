package kea.enter.enterbe.api.vehicle.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetVehicleServiceDto {
    private Long id;

    @Builder
    public GetVehicleServiceDto(Long id) {
        this.id = id;
    }

    public static GetVehicleServiceDto of(Long id) {
        return GetVehicleServiceDto.builder()
            .id(id)
            .build();
    }
}
