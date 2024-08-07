package kea.enter.enterbe.global.algorithm;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ScoreDto {
    private long memberId;
    private long score;

    @Builder
    public ScoreDto(long memberId, long score) {
        this.memberId = memberId;
        this.score = score;
    }

    public static ScoreDto of(long memberId, long score) {
        return ScoreDto.builder()
            .memberId(memberId)
            .score(score)
            .build();
    }

}
