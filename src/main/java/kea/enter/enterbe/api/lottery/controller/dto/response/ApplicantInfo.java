package kea.enter.enterbe.api.lottery.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kea.enter.enterbe.domain.apply.entity.ApplyPurpose;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApplicantInfo {
    @Schema(description = "이메일", example = "shine")
    private String email;

    @Schema(description = "신청자명", example = "이다현")
    private String name;

    @Schema(description = "사용 목적 (TRAVEL, EVENT, HOBBY, SELF, EDUCATION)", example = "콘서트")
    private ApplyPurpose purpose;

    @Schema(description = "당첨 여부 (true/false)", example = "true")
    private Boolean isWinning;

    @Schema(description = "신청 시간", example = "2024-08-03 13:10")
    private String applyTime;

    @Builder
    public ApplicantInfo(String email, String name, ApplyPurpose purpose, Boolean isWinning, String applyTime) {
        this.email = email;
        this.name = name;
        this.purpose = purpose;
        this.isWinning = isWinning;
        this.applyTime = applyTime;
    }

    public static ApplicantInfo of(String email, String name, ApplyPurpose purpose, Boolean isWinning, String applyTime) {
        return ApplicantInfo.builder()
            .email(email)
            .name(name)
            .purpose(purpose)
            .isWinning(isWinning)
            .applyTime(applyTime)
            .build();
    }
}