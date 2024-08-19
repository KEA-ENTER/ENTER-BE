package kea.enter.enterbe.api.member.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import kea.enter.enterbe.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LicenseDto {

    @Schema(description = "사용자 면허 일련번호", example = "123456789012")
    @NotBlank
    @Pattern(regexp = "\\d{12}", message = "12자리의 숫자만 작성되어야 합니다.")
    private String licenseId;

    @Schema(description = "사용저 암호 일련번호(면허증 우측 하단 사진 위)", example = "123ABC")
    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{5,6}$", message = "암호 일련번호는 영문, 숫자를 포함한 5~6자리 텍스트입니다.")
    private String licensePassword;

    @Schema(description = "개인정보 처리 방침 동의 여부", example = "true")
    @NotNull
    @AssertTrue(message = "개인 정보처리 방침에 동의해야 합니다.")
    private Boolean isAgreeTerms;

    @Builder
    public LicenseDto(String licenseId, String licensePassword, Boolean isAgreeTerms) {
        this.licenseId = licenseId;
        this.licensePassword = licensePassword;
        this.isAgreeTerms = isAgreeTerms;
    }

    public static LicenseDto toService(
        String licenseId, String licensePassword, Boolean isAgreeTerm
    ){
        return LicenseDto.builder()
            .licenseId(licenseId)
            .licensePassword(licensePassword)
            .isAgreeTerms(isAgreeTerm)
            .build();
    }

}
