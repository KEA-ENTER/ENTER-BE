package kea.enter.enterbe.api.penalty.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteAdminPenaltyServiceDto {
    private Long memberId;
    private Long penaltyId;

    @Builder
    public DeleteAdminPenaltyServiceDto(Long memberId, Long penaltyId) {
        this.memberId = memberId;
        this.penaltyId = penaltyId;
    }

    public static DeleteAdminPenaltyServiceDto of(Long memberId, Long penaltyId) {
        return DeleteAdminPenaltyServiceDto.builder()
            .memberId(memberId)
            .penaltyId(penaltyId)
            .build();
    }
}
