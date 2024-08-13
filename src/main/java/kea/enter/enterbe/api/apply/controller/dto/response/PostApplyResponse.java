package kea.enter.enterbe.api.apply.controller.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostApplyResponse {
    private String code;
    private String message;

    @Builder
    public PostApplyResponse(String code, String message){
        this.code = code;
        this.message = message;
    }

    public static PostApplyResponse of(String code, String message){
        return PostApplyResponse.builder()
            .code(code)
            .message(message)
            .build();
    }

}
