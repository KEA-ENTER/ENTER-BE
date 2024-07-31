package kea.enter.enterbe.api.vehicle.service.dto;

import kea.enter.enterbe.domain.vehicle.entity.VehicleFuel;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AdminVehicleDto {
    private Long vehicleId;
    private String vehicleNo;
    private String company;
    private String model;
    private int seats;
    private VehicleFuel fuel;
    private String img;
    private VehicleState state;

    @Builder
    public AdminVehicleDto(Long vehicleId, String vehicleNo, String company, String model,
        int seats, VehicleFuel fuel, String img, VehicleState state) {

        this.vehicleId = vehicleId;
        this.vehicleNo = vehicleNo;
        this.company = company;
        this.model = model;
        this.seats = seats;
        this.fuel = fuel;
        this.img = img;
        this.state = state;
    }

    public static AdminVehicleDto of(String vehicleNo, String company, String model,
        int seats, VehicleFuel fuel, String img) {

        return AdminVehicleDto.builder()
            .vehicleNo(vehicleNo)
            .company(company)
            .model(model)
            .seats(seats)
            .fuel(fuel)
            .img(img)
            .state(VehicleState.AVAILABLE)
            .build();
    }
}
