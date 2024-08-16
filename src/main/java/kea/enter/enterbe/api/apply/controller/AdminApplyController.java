package kea.enter.enterbe.api.apply.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kea.enter.enterbe.api.apply.controller.dto.response.GetApplySituationResponse;
import kea.enter.enterbe.api.apply.service.AdminApplyService;
import kea.enter.enterbe.api.apply.service.dto.GetApplySituationServiceDto;
import kea.enter.enterbe.api.lottery.controller.dto.response.GetApplicantListResponse;
import kea.enter.enterbe.global.common.exception.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@Tag(name = "[관리자] 신청 관련 API", description = "Apply")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/applies")
public class AdminApplyController {
    private final AdminApplyService adminApplyService;

    /* 응모 현황 조회 API */
    @Operation(summary = "응모 현황 조회 API", description = "현재 회차 응모 현황을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetApplySituationResponse.class))),
        @ApiResponse(responseCode = "AUT-ERR-010", description = "인증되지 않은 사용자입니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "APR-ERR-001", description = "신청 회차를 찾을 수 없습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "GLB-ERR-003", description = "내부 서버 오류입니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("")
    public ResponseEntity<GetApplySituationResponse> getApplySituation() {
        GetApplySituationServiceDto dto = GetApplySituationServiceDto.of(LocalDate.now());
        GetApplySituationResponse response = adminApplyService.getApplySituation(dto);
        return ResponseEntity.ok(response);
    }
}