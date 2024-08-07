package kea.enter.enterbe.api.apply.controller.dto.response;

import kea.enter.enterbe.domain.apply.entity.ApplyPurpose;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
import kea.enter.enterbe.domain.vehicle.entity.VehicleFuel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class GetApplyDetailResponse {
    private LocalDate takeDate;
    private ApplyPurpose purpose;
    private int competition;
    private String model;
    private VehicleFuel fuel;
    private String company;
    private int seat;
    private String img;

    @Builder
    public GetApplyDetailResponse(LocalDate takeDate, ApplyPurpose purpose, int competition,
    String model, VehicleFuel fuel, String company, int seat, String img){
        this.takeDate = takeDate;
        this.purpose = purpose;
        this.competition = competition;
        this.model = model;
        this.fuel = fuel;
        this.company = company;
        this.seat = seat;
        this.img = img;
    }

    public static GetApplyDetailResponse of(LocalDate takeDate, ApplyPurpose purpose, int competition,
        String model, VehicleFuel fuel, String company, int seat, String img){
        return GetApplyDetailResponse.builder()
            .takeDate(takeDate)
            .purpose(purpose)
            .competition(competition)
            .model(model)
            .fuel(fuel)
            .company(company)
            .seat(seat)
            .img(img)
            .build();
    }

}
