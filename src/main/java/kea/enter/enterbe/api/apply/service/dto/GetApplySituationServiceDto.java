package kea.enter.enterbe.api.apply.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class GetApplySituationServiceDto {
    private LocalDate today;

    @Builder
    public GetApplySituationServiceDto(LocalDate today) {
        this.today = today;
    }

    public static GetApplySituationServiceDto of(LocalDate today) {
        return GetApplySituationServiceDto.builder()
            .today(today)
            .build();
    }
}
