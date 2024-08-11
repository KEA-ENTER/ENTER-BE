package kea.enter.enterbe.api.penalty.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kea.enter.enterbe.domain.penalty.entity.PenaltyReason;
import kea.enter.enterbe.domain.vehicle.entity.VehicleFuel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetPenaltyResponse {
    // penalty table
    @Schema(description = "페널티 아이디", example = "1")
    private Long penaltyId;

    @Schema(description = "페널티 사유 (TAKE, RETURN, BROKEN, FUEL, ETC)", example = "FUEL")
    private PenaltyReason reason;

    @Schema(description = "페널티 수준 (MINIMUM, LOW, MEDIUM, HIGH, BLACKLIST)", example = "LOW")
    private String level;

    @Schema(description = "페널티 비고", example = "24일 21시 경 올림픽대로 280km/h 초과속 운전 범칙금")
    private String etc;

    @Schema(description = "생성일", example = "2024-08-08")
    private String createdAt;

    @Builder
    public GetPenaltyResponse(Long penaltyId, PenaltyReason reason, String level, String etc, String createdAt) {
        this.penaltyId = penaltyId;
        this.reason = reason;
        this.level = level;
        this.etc = etc;
        this.createdAt = createdAt;
    }

    public static GetPenaltyResponse of(Long penaltyId, PenaltyReason reason, String level, String etc, String createdAt) {
        return GetPenaltyResponse.builder()
            .penaltyId(penaltyId)
            .reason(reason)
            .level(level)
            .etc(etc)
            .createdAt(createdAt)
            .build();
    }
}
