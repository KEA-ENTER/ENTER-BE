package kea.enter.enterbe.api.penalty.controller.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetMemberInPenaltyPeriodResponse {

    private boolean isInProgress;
    private int year;
    private int month;
    private int day;

    @Builder
    public GetMemberInPenaltyPeriodResponse(boolean isInProgress, int year, int month, int day) {
        this.isInProgress = isInProgress;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public static GetMemberInPenaltyPeriodResponse of(boolean isInProgress, int year, int month, int day) {
        return GetMemberInPenaltyPeriodResponse.builder()
            .isInProgress(isInProgress)
            .year(year)
            .month(month)
            .day(day)
            .build();
    }

}
