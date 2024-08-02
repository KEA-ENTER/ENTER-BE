package kea.enter.enterbe.api.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenDao {
    @NotNull
    String accessToken;

    @NotNull
    String refreshToken;

    @Builder
    public TokenDao(
        String accessToken, String refreshToken
    ) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static TokenDao of(
        String accessToken, String refreshToken
    ) {
        return TokenDao.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

}
