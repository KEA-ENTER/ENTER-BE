package kea.enter.enterbe.api.penalty.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetPenaltyRequest {
    @Schema(description = "패널티 아이디", example = "1")
    @Positive(message = "양수만 가능합니다.")
    private Long id;
}
