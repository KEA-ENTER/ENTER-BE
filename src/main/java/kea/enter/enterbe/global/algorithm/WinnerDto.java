package kea.enter.enterbe.global.algorithm;

import lombok.Builder;
import lombok.Getter;

@Getter
public class WinnerDto {
    private final long memberId;
    private final int rank;

    @Builder
    public WinnerDto(long memberId, int rank) {
        this.memberId = memberId;
        this.rank = rank;
    }

    public static WinnerDto of(long memberId, int rank) {
        return WinnerDto.builder()
            .memberId(memberId)
            .rank(rank)
            .build();
    }

}
