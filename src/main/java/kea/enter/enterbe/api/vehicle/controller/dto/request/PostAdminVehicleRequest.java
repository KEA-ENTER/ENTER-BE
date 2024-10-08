package kea.enter.enterbe.api.vehicle.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import kea.enter.enterbe.api.vehicle.service.dto.CreateVehicleServiceDto;
import kea.enter.enterbe.domain.vehicle.entity.VehicleFuel;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
public class PostAdminVehicleRequest {
    @Schema(description = "차량 아이디", example = "1")
    @Positive(message = "양수만 가능합니다.")
    private Long id;

    @Schema(description = "차량 번호 (두 자리 또는 세 자리 숫자 + 한글 한 글자 + 네 자리 숫자)", example = "12가 3456")
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

    @Schema(description = "차량 상태 (AVAILABLE, WAIT_TAKE, TAKE_COMPLETE, ON_RENT, WAIT_RETURN, RETURN_COMPLETE, RENT_UNAVAILABLE, INACTIVE)", example = "AVAILABLE")
    @NotNull(message = "차량 상태를 입력해야 합니다.")
    private VehicleState state;


    @Builder
    public PostAdminVehicleRequest(Long id, String vehicleNo, String company, String model, int seats, VehicleFuel fuel, MultipartFile img, VehicleState state) {
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
    public PostAdminVehicleRequest(String vehicleNo, String company, String model, int seats, VehicleFuel fuel, VehicleState state) {
        this.vehicleNo = vehicleNo;
        this.company = company;
        this.model = model;
        this.seats = seats;
        this.fuel = fuel;
        this.state = state;
    }

    @Builder
    public PostAdminVehicleRequest(Long id, String vehicleNo, String company, String model, int seats, VehicleFuel fuel, VehicleState state) {
        this.id = id;
        this.vehicleNo = vehicleNo;
        this.company = company;
        this.model = model;
        this.seats = seats;
        this.fuel = fuel;
        this.state = state;
    }

    public static PostAdminVehicleRequest of(String vehicleNo, String company, String model, int seats, VehicleFuel fuel, MultipartFile img, VehicleState state) {
        return PostAdminVehicleRequest.builder()
            .vehicleNo(vehicleNo)
            .company(company)
            .model(model)
            .seats(seats)
            .fuel(fuel)
            .img(img)
            .state(state)
            .build();
    }

    public CreateVehicleServiceDto toService() {
        return CreateVehicleServiceDto.of(vehicleNo, company, model, seats, fuel, img, state);
    }
}