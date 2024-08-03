package kea.enter.enterbe.api.vehicle.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import kea.enter.enterbe.api.vehicle.service.dto.CreateVehicleDto;
import kea.enter.enterbe.domain.vehicle.entity.VehicleFuel;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
public class AdminVehicleRequest {
    @Schema(description = "차량 아이디", example = "1")
    @Positive(message = "양수만 가능합니다.")
    private Long id;

    @Schema(description = "차량 번호", example = "12가 3456")
    @NotBlank(message = "차량 번호를 입력해야 합니다.")
    private String vehicleNo;

    @Schema(description = "차량 제조사", example = "현대")
    @NotBlank(message = "차량 제조사를 입력해야 합니다.")
    private String company;

    @Schema(description = "차량 모델", example = "그랜저")
    @NotBlank(message = "차량 모델을 입력해야 합니다.")
    private String model;

    @Schema(description = "차량 탑승 정원", example = "5")
    @Positive(message = "양수만 가능합니다.")
    private int seats;

    @Schema(description = "차량 연료 (DIESEL, GASOLINE, ELECTRICITY)", example = "GASOLINE")
    @NotNull(message = "차량 연료 (DIESEL, GASOLINE, ELECTRICITY)를 입력해야 합니다.")
    private VehicleFuel fuel;

    @Schema(description = "차량 이미지", example = "")
    private MultipartFile img;

    @Schema(description = "차량 상태", example = "")
    @NotNull(message = "차량 상태를 입력해야 합니다.")
    private VehicleState state;


    @Builder
    public AdminVehicleRequest(Long id, String vehicleNo, String company, String model, int seats, VehicleFuel fuel, MultipartFile img, VehicleState state) {
        this.id = id;
        this.vehicleNo = vehicleNo;
        this.company = company;
        this.model = model;
        this.seats = seats;
        this.fuel = fuel;
        this.img = img;
        this.state = state;
    }

    @Builder
    public AdminVehicleRequest(String vehicleNo, String company, String model, int seats, VehicleFuel fuel, VehicleState state) {
        this.vehicleNo = vehicleNo;
        this.company = company;
        this.model = model;
        this.seats = seats;
        this.fuel = fuel;
        this.state = state;
    }

    public static AdminVehicleRequest of(String vehicleNo, String company, String model, int seats, VehicleFuel fuel, MultipartFile img, VehicleState state) {
        return AdminVehicleRequest.builder()
            .vehicleNo(vehicleNo)
            .company(company)
            .model(model)
            .seats(seats)
            .fuel(fuel)
            .img(img)
            .state(state)
            .build();
    }

    public CreateVehicleDto toService() {
        return CreateVehicleDto.of(vehicleNo, company, model, seats, fuel, img, state);
    }
}