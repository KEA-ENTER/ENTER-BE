package kea.enter.enterbe.api.penalty.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PenaltyInfo {
    @Schema(description = "페널티 아이디", example = "1")
    private Long penaltyId;

    @Schema(description = "페널티 사유 (미인수, 기간 내 미반납, 차량 훼손, 유류 미달, 기타)", example = "유류 미달")
    private String reason;

    @Schema(description = "페널티 수준 (MINIMUM, LOW, MEDIUM, HIGH, BLACKLIST)", example = "LOW")
    private String level;

    @Schema(description = "생성일", example = "2024-08-08")
    private String createdAt;


    @Builder
    public PenaltyInfo(Long penaltyId, String reason, String level, String createdAt) {
        this.penaltyId = penaltyId;
        this.reason = reason;
        this.level = level;
        this.createdAt = createdAt;
    }

    public static PenaltyInfo of(Long penaltyId, String reason, String level, String createdAt) {
        return PenaltyInfo.builder()
            .penaltyId(penaltyId)
            .reason(reason)
            .level(level)
            .createdAt(createdAt)
            .build();
    }
}
