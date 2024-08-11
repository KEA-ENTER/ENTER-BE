package kea.enter.enterbe.api.vehicle.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import kea.enter.enterbe.domain.vehicle.entity.VehicleFuel;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetAdminVehicleResponse extends VehicleInfo {
//    // vehicle table
////    private VehicleInfo vehicle;
//    @Schema(description = "차량 아이디", example = "1")
//    private Long vehicleId;
//
//    @Schema(description = "차량 번호", example = "12가 3456")
//    private String vehicleNo;
//
//    @Schema(description = "차량 제조사", example = "현대")
//    private String company;
//
//    @Schema(description = "차량 모델", example = "그랜저")
//    private String model;
//
//    @Schema(description = "차량 탑승 정원", example = "5")
//    private int seats;
//
//    @Schema(description = "차량 연료", example = "GASOLINE")
//    private VehicleFuel fuel;
//
//    @Schema(description = "차량 이미지", example = "")
//    private String img;
//
//    @Schema(description = "차량 생성일", example = "2024-08-03")
//    private String createdAt;
//
//    @Schema(description = "차량 수정일", example = "2024-08-03")
//    private String updatedAt;
//
//    @Schema(description = "차량 상태", example = "AVAILABLE")
//    private VehicleState state;
//

    // member table
    @Schema(description = "작성자 이름 목록", example = "[\"김가천\", \"이나천\"]", required = false)
    private List<String> names;

    // vehicle_note table
    @Schema(description = "특이사항 생성일 목록", example = "[\"2024-08-06\", \"2024-08-07\"]", required = false)
    private List<String> reportCreatedAts;

    @Schema(description = "특이사항 내용 목록", example = "[\"차량 내부가 너무 더럽습니다.\", \"차량 내부가 너무 더럽습니다.\"]", required = false)
    private List<String> contents;


    @Builder
    public GetAdminVehicleResponse(Long vehicleId, String vehicleNo, String company, String model,
        int seats, VehicleFuel fuel, String img, String createdAt, String updatedAt,
        VehicleState state, List<String> names, List<String> reportCreatedAts, List<String> contents) {

//        VehicleInfo.builder(vehicle).build();
//        this.vehicle.setVehicleId(vehicleId);
//        this.vehicle.setVehicleNo(vehicleNo);
//        this.vehicle.setCompany(company);
//        this.vehicle.setModel(model);
//        this.vehicle.setSeats(seats);
//        this.vehicle.setFuel(fuel);
//        this.vehicle.setImg(img);
//        this.vehicle.setCreatedAt(createdAt);
//        this.vehicle.setUpdatedAt(updatedAt);
//        this.vehicle.setState(state);
//        this.vehicleId = vehicleId;
//        this.vehicleNo = vehicleNo;
//        this.company = company;
//        this.model = model;
//        this.seats = seats;
//        this.fuel = fuel;
//        this.img = img;
//        this.createdAt = createdAt;
//        this.updatedAt = updatedAt;
//        this.state = state;
        super(vehicleId, vehicleNo, company, model, seats, fuel, img, createdAt, updatedAt, state);

        this.names = names;
        this.reportCreatedAts = reportCreatedAts;
        this.contents = contents;
    }

    public static GetAdminVehicleResponse of(Long vehicleId, String vehicleNo, String company,
        String model, int seats, VehicleFuel fuel, String img, String createdAt, String updatedAt,
        VehicleState state, List<String> names, List<String> reportCreatedAts, List<String> contents) {

        return GetAdminVehicleResponse.builder()
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
            .names(names)
            .reportCreatedAts(reportCreatedAts)
            .contents(contents)
            .build();
    }
}
