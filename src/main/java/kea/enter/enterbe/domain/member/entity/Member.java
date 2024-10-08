package kea.enter.enterbe.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import kea.enter.enterbe.domain.penalty.entity.Penalty;
import kea.enter.enterbe.global.common.entity.BaseEntity;
import kea.enter.enterbe.global.common.entity.CryptoConverter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Convert(converter = CryptoConverter.class)
    @Column(name = "license_id")
    private String licenseId;

    @Convert(converter = CryptoConverter.class)
    @Column(name = "license_password")
    private String licensePassword;

    @Setter
    @Column(name = "is_license_valid")
    private Boolean isLicenseValid;

    @Column(name = "is_agree_terms", nullable = false)
    private Boolean isAgreeTerms;

    @Setter
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
        String name, String email, String password, LocalDate birthDate,
        String licenseId, String licensePassword, Boolean isLicenseValid,
        Boolean isAgreeTerms,
        Integer score, MemberRole role, MemberState state
    ) {
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
        String name, String email, String password, LocalDate birthDate,
        String licenseId, String licensePassword, Boolean isLicenseValid,
        Boolean isAgreeTerms,
        Integer score, MemberRole role, MemberState state
    ) {
        return Member.builder()
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

    public String getBirthDate(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyMMdd");
        return this.birthDate.format(dateTimeFormatter);
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

    // 만 나이를 반환하는 함수
    public Integer getAge(){
        return Period.between(this.birthDate, LocalDate.now()).getYears();
    }
}