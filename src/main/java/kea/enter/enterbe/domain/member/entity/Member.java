package kea.enter.enterbe.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import kea.enter.enterbe.domain.penalty.entity.Penalty;
import kea.enter.enterbe.global.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member extends BaseEntity {
    @Column(name = "employee_number", nullable = false)
    private String employeeNumber;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "license_id")
    private String licenseId;
    @Column(name = "license_password")
    private String licensePassword;
    @Column(name = "license_valid")
    private boolean licenseValid;
    @Column(name = "privacy_consent", nullable = false)
    private boolean privacyConsent;
    @Column(name = "score", nullable = false)
    private int score;
    @Column(name = "role", nullable = false)
    private MemberRole role;
    @Column(name = "state", nullable = false)
    private MemberState state;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Penalty> penaltyList = new ArrayList<>();

    @Builder
    public Member(
        String employeeNumber,
        String name, String email, String password, String licenseId, String licensePassword, boolean licenseValid,
        boolean privacyConsent,
        int score, MemberRole role, MemberState state
    ) {
        this.employeeNumber = employeeNumber;
        this.name = name;
        this.email = email;
        this.password = password;
        this.licenseId = licenseId;
        this.licensePassword = licensePassword;
        this.licenseValid = licenseValid;
        this.privacyConsent = privacyConsent;
        this.score = score;
        this.role = role;
        this.state = state;
    }

    public static Member of(
        String employeeNumber,
        String name, String email, String password,
        String licenseId, String licensePassword, boolean licenseValid,
        boolean privacyConsent,
        int score, MemberRole role, MemberState state
    ) {
        return Member.builder()
            .employeeNumber(employeeNumber)
            .name(name)
            .email(email)
            .password(password)
            .licenseId(licenseId)
            .licensePassword(licensePassword)
            .licenseValid(licenseValid)
            .privacyConsent(privacyConsent)
            .score(score)
            .role(role)
            .state(state)
            .build();
    }
}

