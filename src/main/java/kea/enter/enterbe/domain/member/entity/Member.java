package kea.enter.enterbe.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import kea.enter.enterbe.domain.penalty.entity.Penalty;
import kea.enter.enterbe.global.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member extends BaseEntity {
    @Column(name = "employee_no", nullable = false)
    private String employeeNo;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @JsonFormat(pattern = "yyMMdd")
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "license_id")
    private String licenseId;

    @Column(name = "license_password")
    private String licensePassword;

    @Column(name = "is_license_valid")
    private Boolean isLicenseValid;

    @Column(name = "is_agree_terms", nullable = false)
    private Boolean isAgreeTerms;

    @Column(name = "score", nullable = false)
    private Integer score;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private MemberRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private MemberState state;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Penalty> penaltyList = new ArrayList<>();

    @Builder
    public Member(
        String employeeNo,
        String name, String email, String password, LocalDate birthDate,
        String licenseId, String licensePassword, Boolean isLicenseValid,
        Boolean isAgreeTerms,
        Integer score, MemberRole role, MemberState state
    ) {
        this.employeeNo = employeeNo;
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

    public static Member of(
        String employeeNo,
        String name, String email, String password, LocalDate birthDate,
        String licenseId, String licensePassword, Boolean isLicenseValid,
        Boolean isAgreeTerms,
        Integer score, MemberRole role, MemberState state
    ) {
        return Member.builder()
            .employeeNo(employeeNo)
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

    // 면허 정보를 set하는 함수
    public void setLicenseInformation(
        String licenseId, String licensePassword,
        Boolean isLicenseValid, Boolean isAgreeTerms
    ){
        this.licenseId = licenseId;
        this.licensePassword = licensePassword;
        this.isLicenseValid = isLicenseValid;
        this.isAgreeTerms = isAgreeTerms;
    }
}