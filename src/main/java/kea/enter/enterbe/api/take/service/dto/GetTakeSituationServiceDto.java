package kea.enter.enterbe.api.take.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class GetTakeSituationServiceDto {
    private LocalDate today;

    @Builder
    public GetTakeSituationServiceDto(LocalDate today) {
        this.today = today;
    }

    public static GetTakeSituationServiceDto of(LocalDate today) {
        return GetTakeSituationServiceDto.builder()
            .today(today)
            .build();
    }
}
