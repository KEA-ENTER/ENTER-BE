package kea.enter.enterbe.api.question.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ExampleDto {
    private Long a;
    private Long b;
    @Builder
    public ExampleDto(Long a, Long b) {
        this.a = a;
        this.b = b;
    }

    public static ExampleDto of(Long a, Long b) {
        return ExampleDto.builder()
                .a(a)
                .b(b)
                .build();
    }

}
