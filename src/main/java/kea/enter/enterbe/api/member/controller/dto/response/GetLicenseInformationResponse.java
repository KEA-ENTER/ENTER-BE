package kea.enter.enterbe.api.member.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetLicenseInformationResponse {

    @Schema(description = "클라이언트에게 사용자의 상태를 안내(MEM-001, MEM-002, MEM-003, MEM-004)", example = "MEM-001")
    private String code;
    @Schema(description = "code에 따른 설명(신청 기간이 아닙니다. / 면허증 데이터가 없습니다. / 면허증 진위여부 확인이 필요합니다. / 신청 자격이 확인되었습니다.", example = "신청 기간이 아닙니다.")
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
