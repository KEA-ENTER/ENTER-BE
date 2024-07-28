package kea.enter.enterbe.domain.auth;


import kea.enter.enterbe.domain.member.entity.MemberRole;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LoginUser {
    private long memberId;
    private String email;
    private String name;
    private String password;
    private MemberRole role;

    @Builder
    public LoginUser(long memberId, String email, String name, String password, MemberRole role) {
        this.memberId = memberId;
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public static LoginUser of(long memberId, String email, String name, String password, MemberRole role) {
        return LoginUser.builder().memberId(memberId).email(email).name(name).password(password).role(role).build();
    }
}
