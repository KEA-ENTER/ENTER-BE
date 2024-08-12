package kea.enter.enterbe.api.lottery.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetLotteryResultResponse {

        @Schema(description = "당첨 여부", example = "true")
        private boolean winning;
        @Schema(description = "대기 번호", example = "5")
        private Integer waitingNumber;

        @Builder
        public GetLotteryResultResponse(boolean winning, Integer waitingNumber) {
            this.winning = winning;
            this.waitingNumber = waitingNumber;
        }

        public static GetLotteryResultResponse of(boolean winning, Integer waitingNumber) {
            return GetLotteryResultResponse.builder()
                .winning(winning)
                .waitingNumber(waitingNumber)
                .build();
        }
}

