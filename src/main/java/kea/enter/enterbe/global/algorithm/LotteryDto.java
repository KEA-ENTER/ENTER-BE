package kea.enter.enterbe.global.algorithm;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LotteryDto {
    private List<LotteryMemberDto> winners;
    private List<LotteryMemberDto> waiting;
    private Integer round;

    @Builder
    public LotteryDto(List<LotteryMemberDto> winners, List<LotteryMemberDto> waiting, Integer round) {
        this.winners = winners;
        this.waiting = waiting;
        this.round = round;
    }

    public static LotteryDto of(List<LotteryMemberDto> winners, List<LotteryMemberDto> waiting, Integer round) {
        return LotteryDto.builder()
            .winners(winners)
            .waiting(waiting)
            .round(round)
            .build();
    }

}
