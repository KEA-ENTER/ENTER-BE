package kea.enter.enterbe.api.auth.dto;

import kea.enter.enterbe.domain.member.entity.MemberRole;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberInfoDto {
    private Long memberId;
    private String email;
    private String name;
    private String password;
    private MemberRole role;


    @Builder
    public MemberInfoDto(
        Long memberId, String email, String name, String password, MemberRole role
    ) {
        this.memberId = memberId;
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public static MemberInfoDto of(
        Long memberId, String email, String name, String password, MemberRole role
    ) {
        return MemberInfoDto.builder().memberId(memberId).email(email).name(name).password(password).role(role).build();
    }
}
