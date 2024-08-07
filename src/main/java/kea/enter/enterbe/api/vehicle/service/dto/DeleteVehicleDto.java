package kea.enter.enterbe.api.vehicle.service.dto;

import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DeleteVehicleDto {

    private Long id;

    @Builder
    public DeleteVehicleDto(Long id) {
        this.id = id;
    }

    public static DeleteVehicleDto of(Long id) {
        return DeleteVehicleDto.builder()
            .id(id)
            .build();
    }
}