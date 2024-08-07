package kea.enter.enterbe.api.penalty.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kea.enter.enterbe.domain.penalty.entity.PenaltyLevel;
import kea.enter.enterbe.domain.penalty.entity.PenaltyReason;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetPenaltyListResponse {
    @Schema(description = "페널티 아이디", example = "1")
    private Long penaltyId;

    @Schema(description = "페널티 생성일", example = "2024-07-31")
    private String createdAt;

    @Schema(description = "페널티 사유 (TAKE, RETURN, BROKEN, FUEL, ETC)", example = "FUEL")
    private PenaltyReason reason;

    @Schema(description = "페널티 수준 (LOW, MEDIUM, HIGH, BLACKLIST)", example = "LOW")
    private PenaltyLevel level;

    @Schema(description = "비고", example = "자동차를 박살냈습니다.")
    private String etc;

    @Builder
    public GetPenaltyListResponse(Long penaltyId, String createdAt, PenaltyReason reason,
        PenaltyLevel level, String etc) {
        this.penaltyId = penaltyId;
        this.createdAt = createdAt;
        this.reason = reason;
        this.level = level;
        this.etc = etc;
    }

    public static GetPenaltyListResponse of(Long penaltyId, String createdAt, PenaltyReason reason,
        PenaltyLevel level, String etc) {
        return GetPenaltyListResponse.builder()
            .penaltyId(penaltyId)
            .createdAt(createdAt)
            .reason(reason)
            .level(level)
            .etc(etc)
            .build();
    }
}
