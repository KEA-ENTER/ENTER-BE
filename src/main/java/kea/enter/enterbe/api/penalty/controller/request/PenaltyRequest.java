package kea.enter.enterbe.api.penalty.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kea.enter.enterbe.api.penalty.service.dto.PenaltyDto;
import kea.enter.enterbe.domain.penalty.entity.PenaltyLevel;
import kea.enter.enterbe.domain.penalty.entity.PenaltyReason;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PenaltyRequest {
    @NotNull(message = "페널티 사유를 입력해야 합니다.")
    @Schema(description = "페널티 사유 (TAKE, RETURN, BROKEN, FUEL, ETC)", example = "FUEL")
    private PenaltyReason reason;

    @NotNull(message = "페널티 사유를 입력해야 합니다.")
    @Schema(description = "페널티 사유 (LOW, MEDIUM, HIGH, BLACKLIST)", example = "LOW")
    private PenaltyLevel level;

    @Size(max = 200)
    @Schema(description = "비고", example = "자동차를 박살냈습니다.")
    private String etc;

    @Builder
    public PenaltyRequest(PenaltyReason reason, PenaltyLevel level, String etc) {
        this.reason = reason;
        this.level = level;
        this.etc = etc;
    }

    public static PenaltyRequest of(PenaltyReason reason, PenaltyLevel level, String etc) {
        return PenaltyRequest.builder()
            .reason(reason)
            .level(level)
            .etc(etc)
            .build();
    }

    public PenaltyDto toService() {
        return PenaltyDto.of(reason, level, etc);
    }
}
