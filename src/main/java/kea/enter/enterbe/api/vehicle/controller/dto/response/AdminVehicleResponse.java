package kea.enter.enterbe.api.vehicle.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kea.enter.enterbe.domain.vehicle.entity.VehicleFuel;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class AdminVehicleResponse {
    // vehicle table
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

    @Schema(description = "차량 연료", example = "GASOLINE", required = false)
    private VehicleFuel fuel;

    @Schema(description = "차량 이미지", example = "")
    private String img;

    @Schema(description = "차량 생성일", example = "2024-08-03", required = false)
    private String createdAt;

    @Schema(description = "차량 수정일", example = "2024-08-03", required = false)
    private String updatedAt;

    @Schema(description = "차량 상태", example = "AVAILABLE")
    private VehicleState state;


    // member table
    @Schema(description = "작성자 이름", example = "김가천", required = false)
    private String name;


    // vehicle_note table
    @Schema(description = "특이사항 생성일", example = "2024-08-06", required = false)
    private String reportCreatedAt;

    @Schema(description = "특이사항 내용", example = "차량 내부가 너무 더럽습니다.", required = false)
    private String content;


    @Builder
    public AdminVehicleResponse(Long vehicleId, String vehicleNo, String company, String model,
        int seats, VehicleFuel fuel, String img, String createdAt, String updatedAt,
        VehicleState state, String name, String reportCreatedAt, String content) {

        this.vehicleId = vehicleId;
        this.vehicleNo = vehicleNo;
        this.company = company;
        this.model = model;
        this.seats = seats;
        this.fuel = fuel;
        this.img = img;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.state = state;
        this.name = name;
        this.reportCreatedAt = reportCreatedAt;
        this.content = content;
    }

    public static AdminVehicleResponse ofList(Long vehicleId, String vehicleNo, String company, String model,
        int seats, VehicleFuel fuel, String img, String createdAt, String updatedAt, VehicleState state) {

        return AdminVehicleResponse.builder()
            .vehicleId(vehicleId)
            .vehicleNo(vehicleNo)
            .company(company)
            .model(model)
            .seats(seats)
            .fuel(fuel)
            .img(img)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .state(state)
            .build();
    }

    public static AdminVehicleResponse of(Long vehicleId, String vehicleNo, String company,
        String model, int seats, VehicleFuel fuel, String img, String createdAt, String updatedAt,
        VehicleState state, String name, String reportCreatedAt, String content) {

        return AdminVehicleResponse.builder()
            .vehicleId(vehicleId)
            .vehicleNo(vehicleNo)
            .company(company)
            .model(model)
            .seats(seats)
            .fuel(fuel)
            .img(img)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .state(state)
            .name(name)
            .reportCreatedAt(reportCreatedAt)
            .content(content)
            .build();
    }
}
