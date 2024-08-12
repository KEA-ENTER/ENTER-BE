package kea.enter.enterbe.api.vehicle.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DeleteVehicleServiceDto {

    private Long id;

    @Builder
    public DeleteVehicleServiceDto(Long id) {
        this.id = id;
    }

    public static DeleteVehicleServiceDto of(Long id) {
        return DeleteVehicleServiceDto.builder()
            .id(id)
            .build();
    }
}