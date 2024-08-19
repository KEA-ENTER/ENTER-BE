package kea.enter.enterbe.api.penalty.controller.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.Period;

@Getter
@NoArgsConstructor
public class GetMemberInPenaltyPeriodResponse {

    private boolean isInProgress;
    private Period restOfPenalties;
    private String message;

    @Builder
    public GetMemberInPenaltyPeriodResponse(boolean isInProgress, Period restOfPenalties,
        String message) {
        this.isInProgress = isInProgress;
        this.restOfPenalties = restOfPenalties;
        this.message = message;
    }

    public static GetMemberInPenaltyPeriodResponse of(boolean isInProgress, Period restOfPenalties,
        String message) {
        return GetMemberInPenaltyPeriodResponse.builder()
            .isInProgress(isInProgress)
            .restOfPenalties(restOfPenalties)
            .message(message).build();
    }
}
