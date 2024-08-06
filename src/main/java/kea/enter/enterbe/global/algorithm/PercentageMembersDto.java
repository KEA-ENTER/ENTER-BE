package kea.enter.enterbe.global.algorithm;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PercentageMembersDto {
    private final long memberId;
    private final long percentage;

    @Builder
    public PercentageMembersDto(long memberId, long percentage) {
        this.memberId = memberId;
        this.percentage = percentage;
    }

    public PercentageMembersDto of(long memberId, long percentage) {
        return PercentageMembersDto.builder()
            .memberId(memberId)
            .percentage(percentage)
            .build();
    }
}
