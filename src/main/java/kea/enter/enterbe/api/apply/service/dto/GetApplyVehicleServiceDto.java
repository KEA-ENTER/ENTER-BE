package kea.enter.enterbe.api.apply.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class GetApplyVehicleServiceDto {
    private LocalDate takeDate;
    private LocalDate returnDate;

    @Builder
    public GetApplyVehicleServiceDto(LocalDate takeDate, LocalDate returnDate){
        this.takeDate = takeDate;
        this.returnDate = returnDate;
    }

    public static GetApplyVehicleServiceDto of(LocalDate takeDate, LocalDate returnDate){
        return GetApplyVehicleServiceDto.builder()
            .takeDate(takeDate)
            .returnDate(returnDate)
            .build();
    }



}
