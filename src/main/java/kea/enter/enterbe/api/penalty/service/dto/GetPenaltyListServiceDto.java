package kea.enter.enterbe.api.penalty.service.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
public class GetPenaltyListServiceDto {
    private Long memberId;
    private Pageable pageable;

    @Builder
    public GetPenaltyListServiceDto(Long memberId, Pageable pageable) {
        this.memberId = memberId;
        this.pageable = pageable;
    }

    public static GetPenaltyListServiceDto of(Long memberId, Pageable pageable) {
        return GetPenaltyListServiceDto.builder()
            .memberId(memberId)
            .pageable(pageable)
            .build();
    }
}
