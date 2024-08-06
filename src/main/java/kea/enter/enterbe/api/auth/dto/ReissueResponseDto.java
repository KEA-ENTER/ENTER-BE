package kea.enter.enterbe.api.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReissueResponseDto {
    private String accessToken;
    private String refreshToken;

    @Builder
    public ReissueResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static ReissueResponseDto of(String accessToken, String refreshToken) {
        return ReissueResponseDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

}
