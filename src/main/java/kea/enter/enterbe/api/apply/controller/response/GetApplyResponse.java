package kea.enter.enterbe.api.apply.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class GetApplyResponse {
    private int applyRound;
    private LocalDate takeDate;
    private LocalDate returnDate;

    @Builder
    public GetApplyResponse(int applyRound, LocalDate takeDate, LocalDate returnDate){
        this.applyRound = applyRound;
        this.takeDate = takeDate;
        this.returnDate = returnDate;
    }

    public static GetApplyResponse of(int applyRound, LocalDate takeDate, LocalDate returnDate){
        return GetApplyResponse.builder()
            .applyRound(applyRound)
            .takeDate(takeDate)
            .returnDate(returnDate)
            .build();
    }

}
