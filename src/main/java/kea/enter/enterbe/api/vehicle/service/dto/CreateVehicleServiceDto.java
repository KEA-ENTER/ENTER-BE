package kea.enter.enterbe.api.vehicle.service.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class CreateVehicleServiceDto {
    private String vehicleNo;
    private String company;
    private String model;
    private int seats;
    private String fuel;
    private MultipartFile img;
    private String state;

    @Builder
    public CreateVehicleServiceDto(String vehicleNo, String company, String model,
        int seats, String fuel, MultipartFile img, String state) {
        this.vehicleNo = vehicleNo;
        this.company = company;
        this.model = model;
        this.seats = seats;
        this.fuel = fuel;
        this.img = img;
        this.state = state;
    }

    public static CreateVehicleServiceDto of(String vehicleNo, String company, String model,
        int seats, String fuel, MultipartFile img, String state) {

        return CreateVehicleServiceDto.builder()
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
