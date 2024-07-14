package kea.enter.enterbe.api.controller.ex.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;
import kea.enter.enterbe.api.service.ex.dto.ExampleDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ExampleRequest {

    @Schema(description = "0 이상의 숫자", example = "3")
    @PositiveOrZero(message = "0이상의 숫자만 가능합니다.")
    private Long a;

    @Schema(description = "0 이상의 숫자", example = "5")
    @PositiveOrZero(message = "0이상의 숫자만 가능합니다.")
    private Long b;

    @Builder
    public ExampleRequest(Long a, Long b) {
        this.a = a;
        this.b = b;
    }

    public static ExampleRequest of(Long a, Long b) {
        return ExampleRequest.builder().a(a).b(b).build();
    }

    public ExampleDto toService() {
        return ExampleDto.of(a,b);
    }
}
