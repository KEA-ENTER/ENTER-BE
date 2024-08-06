package kea.enter.enterbe.api.auth.dto;

import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.entity.MemberRole;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberInfoDto {
    private Long id;
    private String email;
    private String name;
    private String password;
    private MemberRole role;

    @Builder
    public MemberInfoDto(
        Long id, String email, String name, String password, MemberRole role
    ) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public static MemberInfoDto of(
        Long id, String email, String name, String password, MemberRole role
    ) {
        return MemberInfoDto.builder().id(id).email(email).name(name).password(password).role(role).build();
    }

    public static MemberInfoDto toDto(Member member) {
        return MemberInfoDto.builder()
            .id(member.getId())
            .email(member.getEmail())
            .name(member.getName())
            .password(member.getPassword())
            .role(member.getRole())
            .build();
    }
}