package kea.enter.enterbe.api.vehicle.service.dto;

import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetVehicleListDto {
    private String vehicleNo;
    private String model;
    private VehicleState state;

    @Builder
    public GetVehicleListDto(String vehicleNo, String model, VehicleState state) {
        this.vehicleNo = vehicleNo;
        this.model = model;
        this.state = state;
    }

    public static GetVehicleListDto of(String vehicleNo, String model, VehicleState state) {
        return GetVehicleListDto.builder()
            .vehicleNo(vehicleNo)
            .model(model)
            .state(state)
            .build();
    }
}
