package kea.enter.enterbe.global.algorithm;

import java.time.LocalDate;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.entity.MemberRole;
import kea.enter.enterbe.domain.member.entity.MemberState;
import kea.enter.enterbe.global.common.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class TestMemberDto extends BaseEntity {
    private String name;

    private String email;

    @Setter
    private String password;

    private String birthDate;

    private String licenseId;

    private String licensePassword;

    private Boolean isLicenseValid;

    private Boolean isAgreeTerms;

    private Integer score;

    private MemberRole role;

    private MemberState state;

    @Builder
    public TestMemberDto(String name, String email, String password, String birthDate, String licenseId, String licensePassword, Boolean isLicenseValid, Boolean isAgreeTerms, Integer score, MemberRole role, MemberState state) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.licenseId = licenseId;
        this.licensePassword = licensePassword;
        this.isLicenseValid = isLicenseValid;
        this.isAgreeTerms = isAgreeTerms;
        this.score = score;
        this.role = role;
        this.state = state;
    }

    public TestMemberDto of(String name, String email, String password, String birthDate, String licenseId, String licensePassword, Boolean isLicenseValid, Boolean isAgreeTerms, Integer score, MemberRole role, MemberState state) {
        return TestMemberDto.builder()
            .name(name)
            .email(email)
            .password(password)
            .birthDate(birthDate)
            .licenseId(licenseId)
            .licensePassword(licensePassword)
            .isLicenseValid(isLicenseValid)
            .isAgreeTerms(isAgreeTerms)
            .score(score)
            .role(role)
            .state(state)
            .build();
    }

    public Member dtoToMember(TestMemberDto dto) {
        return Member.of(dto.getName(), dto.getEmail(), dto.getPassword(), LocalDate.parse(dto.getBirthDate()), dto.getLicenseId(), dto.getLicensePassword(), dto.getIsLicenseValid(), dto.getIsAgreeTerms(), dto.getScore(), dto.getRole(), dto.getState());
    }
}
