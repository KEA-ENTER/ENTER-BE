package kea.enter.enterbe.api.lottery.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ScoreDto {
    private Long memberId;
    private Long score;

    @Builder
    public ScoreDto(Long memberId, Long score) {
        this.memberId = memberId;
        this.score = score;
    }

    public static ScoreDto of(Long memberId, Long score) {
        return ScoreDto.builder()
            .memberId(memberId)
            .score(score)
            .build();
    }
}
