package kea.enter.enterbe.api.lottery.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kea.enter.enterbe.api.lottery.controller.dto.request.GetApplicantListRequest;
import kea.enter.enterbe.api.lottery.controller.dto.request.GetLotteryListRequest;
import kea.enter.enterbe.api.lottery.controller.dto.response.GetApplicantListResponse;
import kea.enter.enterbe.api.lottery.controller.dto.response.GetLotteryListResponse;
import kea.enter.enterbe.api.lottery.service.AdminLotteryService;
import kea.enter.enterbe.global.common.exception.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "[관리자] 추첨 관련 API", description = "Lottery")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/lotteries")
public class AdminLotteryController {
    private final AdminLotteryService adminLotteryService;

    /* 추첨 관리 목록 조회 API */
    @Operation(summary = "추첨 관리 목록 조회 API", description = "그동안의 추첨에 대한 통계를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "AUT-ERR-010", description = "인증되지 않은 사용자입니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "GLB-ERR-003", description = "내부 서버 오류입니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("")
    public ResponseEntity<GetLotteryListResponse> getLotteryList(
        @Valid @ParameterObject GetLotteryListRequest request,
        @ParameterObject Pageable pageable) {
        GetLotteryListResponse response = adminLotteryService.getLotteryList(request.toService(pageable));
        return ResponseEntity.ok(response);
    }

    /* 신청 내역 목록 조회 API */
    @Operation(summary = "신청 내역 목록 조회 API", description = "해당 추첨에 대한 신청자 내역을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetApplicantListResponse.class))),
        @ApiResponse(responseCode = "AUT-ERR-010", description = "인증되지 않은 사용자입니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "APR-ERR-001", description = "신청 회차를 찾을 수 없습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "GLB-ERR-003", description = "내부 서버 오류입니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{applyRoundId}")
    public ResponseEntity<GetApplicantListResponse> getApplicantList(
        @PathVariable Long applyRoundId,
        @Valid @ParameterObject GetApplicantListRequest request,
        @ParameterObject Pageable pageable) {
        GetApplicantListResponse response = adminLotteryService.getApplicantList(request.toService(applyRoundId, pageable));
        return ResponseEntity.ok(response);
    }
}