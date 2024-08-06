package kea.enter.enterbe.api.apply.controller.response;

import kea.enter.enterbe.domain.vehicle.entity.VehicleFuel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetApplyVehicleResponse {
    private int competition;
    private String model;
    private VehicleFuel fuel;
    private String company;
    private int seat;
    private String img;

    @Builder
    public GetApplyVehicleResponse(int competition, String model, VehicleFuel fuel,
        String company, int seat, String img){
        this.competition = competition;
        this.model = model;
        this.fuel = fuel;
        this.company = company;
        this.seat = seat;
        this.img = img;
    }

    public static GetApplyVehicleResponse of(int competition, String model, VehicleFuel fuel,
        String company, int seat, String img){
        return GetApplyVehicleResponse.builder()
            .competition(competition)
            .model(model)
            .fuel(fuel)
            .company(company)
            .seat(seat)
            .img(img)
            .build();
    }

}
