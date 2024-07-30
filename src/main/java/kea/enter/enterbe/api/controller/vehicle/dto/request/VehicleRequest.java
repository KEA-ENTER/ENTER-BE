package kea.enter.enterbe.api.controller.vehicle.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import kea.enter.enterbe.api.service.vehicle.dto.VehicleDto;
import kea.enter.enterbe.domain.vehicle.entity.VehicleFuel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VehicleRequest {
    @Schema(description = "차량 번호", example = "12가 3456")
    private String vehicleNo;

    @Schema(description = "차량 제조사", example = "현대")
    private String company;

    @Schema(description = "차량 모델", example = "그랜저")
    private String model;

    @Schema(description = "차량 탑승 정원", example = "5")
    @Positive(message = "양수만 가능합니다.")
    private int seats;

    @Schema(description = "차량 연료", example = "가솔린")
    private VehicleFuel fuel;

    @Schema(description = "차량 이미지", example = "")
    private String img;


    @Builder
    public VehicleRequest(String vehicleNo, String company, String model, int seats, VehicleFuel fuel, String img) {
        this.vehicleNo = vehicleNo;
        this.company = company;
        this.model = model;
        this.seats = seats;
        this.fuel = fuel;
        this.img = img;
    }

    public static VehicleRequest of(String vehicleNo, String company, String model, int seats, VehicleFuel fuel, String img) {
        return VehicleRequest.builder()
            .vehicleNo(vehicleNo)
            .company(company)
            .model(model)
            .seats(seats)
            .fuel(fuel)
            .img(img)
            .build();
    }

    public VehicleDto toService() {
        return VehicleDto.of(vehicleNo, company, model, seats, fuel, img);
    }
}