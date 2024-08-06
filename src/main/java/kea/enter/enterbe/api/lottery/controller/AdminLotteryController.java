package kea.enter.enterbe.api.lottery.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kea.enter.enterbe.api.lottery.controller.dto.request.GetApplicantListRequest;
import kea.enter.enterbe.api.lottery.controller.dto.request.GetLotteryListRequest;
import kea.enter.enterbe.api.lottery.controller.dto.response.GetApplicantListResponse;
import kea.enter.enterbe.api.lottery.controller.dto.response.GetLotteryListResponse;
import kea.enter.enterbe.api.lottery.service.AdminLotteryService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "당첨", description = "[관리자] Lottery")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/lotteries")
public class AdminLotteryController {
    private final AdminLotteryService adminLotteryService;

    /* 추첨 관리 목록 조회 API */
    @Operation(summary = "추첨 관리 목록 조회 API", description = "그동안의 추첨에 대한 통계를 조회합니다.")
    @GetMapping("")
    public ResponseEntity<GetLotteryListResponse> getLotteryList(
        @Valid @ParameterObject GetLotteryListRequest request,
        @ParameterObject Pageable pageable) {
        // TODO: 어드민 권한 검사
        GetLotteryListResponse response = adminLotteryService.getLotteryList(request.toService(pageable));
        return ResponseEntity.ok(response);
    }

    /* 신청 내역 목록 조회 API */
    @Operation(summary = "신청 내역 목록 조회 API", description = "해당 추첨에 대한 신청자 내역을 조회합니다.")
    @GetMapping("/{applyRoundId}")
    public ResponseEntity<GetApplicantListResponse> getApplicantList(
        @PathVariable Long applyRoundId,
        @Valid @ParameterObject GetApplicantListRequest request,
        @ParameterObject Pageable pageable) {
        // TODO: 어드민 권한 검사
        GetApplicantListResponse response = adminLotteryService.getApplicantList(request.toService(applyRoundId, pageable));
        return ResponseEntity.ok(response);
    }
}