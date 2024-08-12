package kea.enter.enterbe.api.member.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetLicenseInformationResponse {

    private String code;
    private String message;

    @Builder
    public GetLicenseInformationResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static GetLicenseInformationResponse of(String code, String message) {
        return GetLicenseInformationResponse.builder().code(code).message(message).build();
    }

}
