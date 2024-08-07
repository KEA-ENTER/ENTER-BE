package kea.enter.enterbe.api.apply.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class GetApplyServiceDto {
    private LocalDate today;

    @Builder
    public GetApplyServiceDto(LocalDate today){
        this.today = today;
    }

    public static GetApplyServiceDto of(LocalDate today){
        return GetApplyServiceDto.builder()
            .today(today)
            .build();
    }

}
