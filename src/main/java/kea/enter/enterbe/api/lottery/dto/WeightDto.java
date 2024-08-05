package kea.enter.enterbe.api.lottery.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class WeightDto {
    private Long memberId;
    private Long weight;

    @Builder
    public WeightDto(Long memberId, Long weight) {
        this.memberId = memberId;
        this.weight = weight;
    }

    public static WeightDto of(Long memberId, Long weight) {
        return WeightDto.builder()
            .memberId(memberId)
            .weight(weight)
            .build();
    }
}
