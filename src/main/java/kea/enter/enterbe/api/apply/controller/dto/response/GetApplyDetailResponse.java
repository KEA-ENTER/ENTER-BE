package kea.enter.enterbe.api.apply.controller.dto.response;

import kea.enter.enterbe.domain.apply.entity.ApplyPurpose;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class GetApplyDetailResponse {
    private LocalDate takeDate;
    private ApplyPurpose purpose;
    private Vehicle vehicle;
    private int competition;

    @Builder
    public GetApplyDetailResponse(LocalDate takeDate, ApplyPurpose purpose,
        Vehicle vehicle, int competition){
        this.takeDate = takeDate;
        this.purpose = purpose;
        this.vehicle = vehicle;
        this.competition = competition;
    }

    public static GetApplyDetailResponse of(LocalDate takeDate, ApplyPurpose purpose,
        Vehicle vehicle, int competition){
        return GetApplyDetailResponse.builder()
            .takeDate(takeDate)
            .purpose(purpose)
            .vehicle(vehicle)
            .competition(competition)
            .build();
    }

}
