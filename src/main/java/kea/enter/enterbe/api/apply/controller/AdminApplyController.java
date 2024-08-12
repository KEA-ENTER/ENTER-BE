package kea.enter.enterbe.api.apply.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kea.enter.enterbe.api.apply.controller.dto.response.GetApplySituationResponse;
import kea.enter.enterbe.api.apply.service.AdminApplyService;
import kea.enter.enterbe.api.apply.service.dto.GetApplySituationServiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@Tag(name = "신청 관련 API", description = "[관리자] Apply")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/applies")
public class AdminApplyController {
    private final AdminApplyService adminApplyService;

    /* 응모 현황 조회 API */
    @Operation(summary = "응모 현황 조회 API", description = "현재 회차 응모 현황을 조회합니다.")
    @GetMapping("")
    public ResponseEntity<GetApplySituationResponse> getApplySituation() {
        GetApplySituationServiceDto dto = GetApplySituationServiceDto.of(LocalDate.now());
        GetApplySituationResponse response = adminApplyService.getApplySituation(dto);
        return ResponseEntity.ok(response);
    }
}