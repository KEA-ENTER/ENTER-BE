package kea.enter.enterbe.api.auth.dto;

import jakarta.validation.constraints.NotNull;
import kea.enter.enterbe.domain.member.entity.MemberRole;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponseDto {
    @NotNull
    String accessToken;

    @NotNull
    String refreshToken;

    @NotNull
    String memberName;

    @NotNull
    MemberRole memberRole;

    @Builder
    public LoginResponseDto(
        String accessToken, String refreshToken, String memberName, MemberRole memberRole
    ) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.memberName = memberName;
        this.memberRole = memberRole;
    }

    public static LoginResponseDto of(
        String accessToken, String refreshToken, String memberName, MemberRole memberRole
    ) {
        return LoginResponseDto.builder().accessToken(accessToken).refreshToken(refreshToken).memberName(memberName).memberRole(memberRole).build();
    }

}
