package kea.enter.enterbe.api.apply.controller.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class GetApplyResponse {
    private int round;
    private LocalDate takeDate;
    private LocalDate returnDate;

    @Builder
    public GetApplyResponse(int round, LocalDate takeDate, LocalDate returnDate){
        this.round = round;
        this.takeDate = takeDate;
        this.returnDate = returnDate;
    }

    public static GetApplyResponse of(int round, LocalDate takeDate, LocalDate returnDate){
        return GetApplyResponse.builder()
            .round(round)
            .takeDate(takeDate)
            .returnDate(returnDate)
            .build();
    }

}
