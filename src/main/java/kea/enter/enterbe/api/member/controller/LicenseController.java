package kea.enter.enterbe.api.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kea.enter.enterbe.api.member.controller.dto.request.LicenseDto;
import kea.enter.enterbe.api.member.controller.dto.response.GetLicenseInformationResponse;
import kea.enter.enterbe.api.member.service.LicenseService;
import kea.enter.enterbe.global.common.api.CustomResponseCode;
import kea.enter.enterbe.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "[사용자] 면허 관련 API", description = "License")
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class LicenseController {

    private final LicenseService licenseService;

    // 면허 여부 조회
    @Operation(
        summary = "면허를 포함한 신청 자격 확인 API",
        description = "신청 기간(월요일 오전9시~화요일 밤12시),면허증 데이터를 체크합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "MEM-001(신청 기간이 아닙니다.)", content = @Content(schema = @Schema(implementation = GetLicenseInformationResponse.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "200", description = "MEM-002(면허증 데이터가 없습니다.)", content = @Content(schema = @Schema(implementation = GetLicenseInformationResponse.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "200", description = "MEM-003(면허증 진위여부 확인이 필요합니다.)", content = @Content(schema = @Schema(implementation = GetLicenseInformationResponse.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "200", description = "MEM-004(신청 자격이 확인되었습니다.)", content = @Content(schema = @Schema(implementation = GetLicenseInformationResponse.class), mediaType = "application/json")),
    })
    @GetMapping("/license")
    public ResponseEntity<GetLicenseInformationResponse> getLicenseInformation(Authentication authentication) {

        Long memberId = Long.valueOf(authentication.getName());

        return ResponseEntity.ok(licenseService.getLicenseInformation(memberId));
    }

    // 면허증 등록 (등록 페이지에서 호출)
    @Operation(summary = "면허증 등록 API", description = "면허증 진위여부를 확인하고 등록합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "MEM-ERR-004 / 면허 정보가 유효하지 않습니다.", content = @Content(schema = @Schema(implementation = CustomException.class), mediaType = "application/json"))
    })
    @PostMapping("/license")
    public ResponseEntity<CustomResponseCode> postLicenseInformation(
        Authentication authentication,
        @Valid @RequestBody LicenseDto licenseDto)
    {
        Long memberId = Long.valueOf(authentication.getName());
        licenseService.saveLicenseInformation(memberId, licenseDto);

        return ResponseEntity.ok(CustomResponseCode.SUCCESS);
    }

    // 면허 진위여부 확인 및 isLicenseValid 데이터 patch
    @Operation(summary = "면허 진위여부 수정 API", description = "DB에 저장된 면허 데이터의 진위여부를 확인하고 저장합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "MEM-ERR-004 / 면허 정보가 유효하지 않습니다.", content = @Content(schema = @Schema(implementation = CustomException.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "MEM-ERR-002 / 면허 정보를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = CustomException.class), mediaType = "application/json")),

    })
    @PatchMapping("/valid-license")
    public ResponseEntity<CustomResponseCode> patchLicenseInformation(Authentication authentication){

        Long memberId = Long.valueOf(authentication.getName());
        licenseService.patchLicenseInformation(memberId);

        return ResponseEntity.ok(CustomResponseCode.SUCCESS);
    }
}
