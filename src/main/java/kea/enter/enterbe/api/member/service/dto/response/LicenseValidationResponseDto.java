package kea.enter.enterbe.api.member.service.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LicenseValidationResponseDto {

    private String resUserNm;
    private String commBirthDate;
    private String resLicenseNumber;
    private String resAuthenticity;
    private String resAuthenticityDesc1;
    private String resAuthenticityDesc2;
    private String resSearchDateTime;

    @Builder
    public LicenseValidationResponseDto(String resUserNm, String commBirthDate,
        String resLicenseNumber, String resAuthenticity, String resAuthenticityDesc1,
        String resAuthenticityDesc2, String resSearchDateTime) {
        this.resUserNm = resUserNm;
        this.commBirthDate = commBirthDate;
        this.resLicenseNumber = resLicenseNumber;
        this.resAuthenticity = resAuthenticity;
        this.resAuthenticityDesc1 = resAuthenticityDesc1;
        this.resAuthenticityDesc2 = resAuthenticityDesc2;
        this.resSearchDateTime = resSearchDateTime;
    }

    public static LicenseValidationResponseDto of(String resUserNm, String commBirthDate,
        String resLicenseNumber, String resAuthenticity, String resAuthenticityDesc1,
        String resAuthenticityDesc2, String resSearchDateTime) {
        return LicenseValidationResponseDto.builder()
            .resUserNm(resUserNm)
            .commBirthDate(commBirthDate)
            .resLicenseNumber(resLicenseNumber)
            .resAuthenticity(resAuthenticity)
            .resAuthenticityDesc1(resAuthenticityDesc1)
            .resAuthenticityDesc2(resAuthenticityDesc2)
            .resSearchDateTime(resSearchDateTime)
            .build();
    }
}
