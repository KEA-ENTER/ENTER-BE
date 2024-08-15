package kea.enter.enterbe.api.lottery.service.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
    public class GetLotteryServiceDto {
    private Long memberId;
    private Pageable pageable;

    @Builder
    public GetLotteryServiceDto (Long memberId, Pageable pageable) {
        this.memberId = memberId;
        this.pageable = pageable;
    }

    public static GetLotteryServiceDto of(Long memberId, Pageable pageable) {
        return GetLotteryServiceDto.builder()
            .memberId(memberId)
            .pageable(pageable)
            .build();
    }
}
