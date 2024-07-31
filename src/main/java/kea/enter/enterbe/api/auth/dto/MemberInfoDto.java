package kea.enter.enterbe.api.auth.dto;

import kea.enter.enterbe.domain.member.entity.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberInfoDto {
    private Long memberId;
    private String email;
    private String name;
    private String password;
    private MemberRole role;
}
