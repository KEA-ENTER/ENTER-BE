package kea.enter.enterbe.api.penalty.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetAdminPenaltyListServiceDto {
    private Long memberId;

    @Builder
    public GetAdminPenaltyListServiceDto(Long memberId) {
        this.memberId = memberId;
    }

    public static GetAdminPenaltyListServiceDto of(Long memberId) {
        return GetAdminPenaltyListServiceDto.builder()
            .memberId(memberId)
            .build();
    }
}
