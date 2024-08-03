package kea.enter.enterbe.api.vehicle.service.dto;

import kea.enter.enterbe.domain.vehicle.entity.VehicleFuel;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class ModifyVehicleDto {
    private Long id;
    private String vehicleNo;
    private String company;
    private String model;
    private int seats;
    private VehicleFuel fuel;
    private MultipartFile img;
    private VehicleState state;

    @Builder
    public ModifyVehicleDto(Long id, String vehicleNo, String company, String model,
        int seats, VehicleFuel fuel, MultipartFile img, VehicleState state) {

        this.id = id;
        this.vehicleNo = vehicleNo;
        this.company = company;
        this.model = model;
        this.seats = seats;
        this.fuel = fuel;
        this.img = img;
        this.state = state;
    }

    public static ModifyVehicleDto of(String vehicleNo, String company, String model,
        int seats, VehicleFuel fuel, MultipartFile img, VehicleState state) {

        return ModifyVehicleDto.builder()
            .vehicleNo(vehicleNo)
            .company(company)
            .model(model)
            .seats(seats)
            .fuel(fuel)
            .img(img)
            .state(state)
            .build();
    }
}
