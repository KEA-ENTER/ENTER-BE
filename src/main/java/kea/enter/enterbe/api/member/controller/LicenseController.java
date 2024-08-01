package kea.enter.enterbe.api.member.controller;

import jakarta.validation.Valid;
import kea.enter.enterbe.api.member.controller.dto.request.LicenseDto;
import kea.enter.enterbe.api.member.service.LicenseService;
import kea.enter.enterbe.global.common.api.CustomResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO: swagger 연결을 위한 태그, 이후 수정
//@Tag(name = "예제", description = "예제 API 명세서")
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class LicenseController {

    private final LicenseService licenseService;

    // 면허 여부 조회
    @GetMapping("/license")
    public ResponseEntity<CustomResponseCode> getLicenseInformation() {
        Long memberId = 1L;
        licenseService.getLicenseInformation(memberId);
        return ResponseEntity.ok(CustomResponseCode.SUCCESS);
    }

    // 면허증 등록 (등록 페이지에서 호출)
    @PostMapping("/license")
    public ResponseEntity<CustomResponseCode> postLicenseInformation(@Valid @RequestBody LicenseDto licenseDto)
    {
        Long memberId = 1L;
        licenseService.saveLicenseInformation(
            LicenseDto.builder()
                .memberId(memberId)
                .licenseId(licenseDto.getLicenseId())
                .licensePassword(licenseDto.getLicensePassword())
                .privateDataAgree(licenseDto.getPrivateDataAgree())
                .build()
        );
        return ResponseEntity.ok(CustomResponseCode.SUCCESS);
    }
}
