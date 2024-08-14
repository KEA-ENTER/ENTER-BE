package kea.enter.enterbe.api.penalty.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kea.enter.enterbe.domain.penalty.entity.PenaltyReason;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetPenaltyListResponse {
    @Schema(description = "페널티 아이디", example = "1")
    private Long penaltyId;

    @Schema(description = "페널티 사유 (TAKE, RETURN, BROKEN, FUEL, ETC)", example = "FUEL")
    private String reason;

    @Schema(description = "페널티 수준 (MINIMUM, LOW, MEDIUM, HIGH, BLACKLIST)", example = "LOW")
    private String level;

    @Schema(description = "생성일", example = "2024-08-08")
    private String createdAt;


    @Builder
    public GetPenaltyListResponse(Long penaltyId, String reason, String level, String createdAt) {
        this.penaltyId = penaltyId;
        this.reason = reason;
        this.level = level;
        this.createdAt = createdAt;
    }

    public static GetPenaltyListResponse of(Long penaltyId, String reason, String level, String createdAt) {
        return GetPenaltyListResponse.builder()
            .penaltyId(penaltyId)
            .reason(reason)
            .level(level)
            .createdAt(createdAt)
            .build();
    }
}
