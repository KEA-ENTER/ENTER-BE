package kea.enter.enterbe.api.controller.vehicle.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kea.enter.enterbe.domain.vehicle.entity.VehicleFuel;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import lombok.Builder;
import lombok.Getter;

@Getter
public class VehicleResponse {
    @Schema(description = "차량 아이디", example = "1")
    private Long vehicleId;

    @Schema(description = "차량 번호", example = "12가 3456")
    private String vehicleNo;

    @Schema(description = "차량 제조사", example = "현대")
    private String company;

    @Schema(description = "차량 모델", example = "그랜저")
    private String model;

    @Schema(description = "차량 탑승 정원", example = "5")
    private int seats;

    @Schema(description = "차량 연료", example = "가솔린")
    private VehicleFuel fuel;

    @Schema(description = "차량 이미지", example = "")
    private String img;

    @Schema(description = "차량 상태", example = "AVAILABLE")
    private VehicleState state;


    @Builder
    public VehicleResponse(Long vehicleId, String vehicleNo, String company, String model,
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

    public static VehicleResponse of(Long vehicleId, String vehicleNo, String company, String model,
        int seats, VehicleFuel fuel, String img, VehicleState state) {

        return VehicleResponse.builder()
            .vehicleId(vehicleId)
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
