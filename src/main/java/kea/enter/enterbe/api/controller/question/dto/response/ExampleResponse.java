package kea.enter.enterbe.api.controller.question.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ExampleResponse {
    @Schema(description = "a와 b의 합",example = "8")
    private Long sum;

    @Builder
    public ExampleResponse(Long sum) {
        this.sum = sum;
    }
    public static ExampleResponse of(Long sum) {
        return ExampleResponse.builder().sum(sum).build();
    }
}
