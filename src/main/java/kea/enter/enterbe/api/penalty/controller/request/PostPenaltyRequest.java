package kea.enter.enterbe.api.penalty.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kea.enter.enterbe.api.penalty.service.dto.PostPenaltyServiceDto;
import kea.enter.enterbe.domain.penalty.entity.PenaltyLevel;
import kea.enter.enterbe.domain.penalty.entity.PenaltyReason;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostPenaltyRequest {
    @NotNull(message = "페널티 사유를 입력해야 합니다.")
    @Schema(description = "페널티 사유 (TAKE, RETURN, BROKEN, FUEL, ETC)", example = "FUEL")
    private PenaltyReason reason;

    @NotNull(message = "페널티 수준을 입력해야 합니다.")
    @Schema(description = "페널티 수준 (LOW, MEDIUM, HIGH, BLACKLIST)", example = "LOW")
    private PenaltyLevel level;

    @Size(max = 200)
    @Schema(description = "비고", example = "자동차를 박살냈습니다.")
    private String etc;

    @Builder
    public PostPenaltyRequest(PenaltyReason reason, PenaltyLevel level, String etc) {
        this.reason = reason;
        this.level = level;
        this.etc = etc;
    }

    public static PostPenaltyRequest of(PenaltyReason reason, PenaltyLevel level, String etc) {
        return PostPenaltyRequest.builder()
            .reason(reason)
            .level(level)
            .etc(etc)
            .build();
    }

    public PostPenaltyServiceDto toService(Long memberId) {
        return PostPenaltyServiceDto.of(memberId, reason, level, etc);
    }
}
