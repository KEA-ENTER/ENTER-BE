package kea.enter.enterbe.api.member.service.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LicenseValidationRequestDto {

    private String organization;
    private String birthDate;
    private String licenseNo01;
    private String licenseNo02;
    private String licenseNo03;
    private String licenseNo04;
    private String serialNo;
    private String userName;

    @Builder
    public LicenseValidationRequestDto(String organization, String birthDate, String licenseNo01, String licenseNo02,
        String licenseNo03, String licenseNo04, String serialNo, String userName) {
        this.organization = organization;
        this.birthDate = birthDate;
        this.licenseNo01 = licenseNo01;
        this.licenseNo02 = licenseNo02;
        this.licenseNo03 = licenseNo03;
        this.licenseNo04 = licenseNo04;
        this.serialNo = serialNo;
        this.userName = userName;
    }

    public static LicenseValidationRequestDto of(String organization, String birthDate, String licenseNo01, String licenseNo02,
        String licenseNo03, String licenseNo04, String serialNo, String userName) {
        return LicenseValidationRequestDto.builder()
            .organization(organization)
            .birthDate(birthDate)
            .licenseNo01(licenseNo01)
            .licenseNo02(licenseNo02)
            .licenseNo03(licenseNo03)
            .licenseNo04(licenseNo04)
            .serialNo(serialNo)
            .userName(userName)
            .build();
    }

}
