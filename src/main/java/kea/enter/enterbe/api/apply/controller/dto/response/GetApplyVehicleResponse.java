package kea.enter.enterbe.api.apply.controller.dto.response;

import kea.enter.enterbe.domain.vehicle.entity.VehicleFuel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetApplyVehicleResponse {
    private Long applyRoundId;
    private Long vehicleId;
    private int round;
    private int competition;
    private String model;
    private VehicleFuel fuel;
    private String company;
    private int seat;
    private String img;

    @Builder
    public GetApplyVehicleResponse(Long applyRoundId, Long vehicleId, int round, int competition, String model, VehicleFuel fuel,
        String company, int seat, String img){
        this.applyRoundId = applyRoundId;
        this.vehicleId = vehicleId;
        this.round = round;
        this.competition = competition;
        this.model = model;
        this.fuel = fuel;
        this.company = company;
        this.seat = seat;
        this.img = img;
    }

    public static GetApplyVehicleResponse of(Long applyRoundId, Long vehicleId, int round, int competition, String model, VehicleFuel fuel,
        String company, int seat, String img){
        return GetApplyVehicleResponse.builder()
            .applyRoundId(applyRoundId)
            .vehicleId(vehicleId)
            .round(round)
            .competition(competition)
            .model(model)
            .fuel(fuel)
            .company(company)
            .seat(seat)
            .img(img)
            .build();
    }

}
