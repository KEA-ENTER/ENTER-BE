package kea.enter.enterbe.api.penalty.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeletePenaltyServiceDto {
    private Long memberId;
    private Long penaltyId;

    @Builder
    public DeletePenaltyServiceDto(Long memberId, Long penaltyId) {
        this.memberId = memberId;
        this.penaltyId = penaltyId;
    }

    public static DeletePenaltyServiceDto of(Long memberId, Long penaltyId) {
        return DeletePenaltyServiceDto.builder()
            .memberId(memberId)
            .penaltyId(penaltyId)
            .build();
    }
}
