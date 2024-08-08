package kea.enter.enterbe.api.take.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kea.enter.enterbe.api.take.controller.dto.response.GetReturnReportResponse;
import kea.enter.enterbe.api.take.controller.dto.response.GetTakeSituationResponse;
import kea.enter.enterbe.api.take.service.AdminTakeService;
import kea.enter.enterbe.api.take.service.dto.GetReturnReportServiceDto;
import kea.enter.enterbe.api.take.service.dto.GetTakeSituationServiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;

@Tag(name = "인수", description = "[관리자] Take")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/takes")
public class AdminTakeController {
    private final AdminTakeService adminTakeService;

    /* 인수 현황 조회 API */
    @Operation(summary = "인수 현황 조회 API", description = "이전 회차 인수 현황을 조회합니다.")
    @GetMapping("")
    public ResponseEntity<GetTakeSituationResponse> getTakeSituation() {
        // TODO: 어드민 권한 검사
        GetTakeSituationServiceDto dto = GetTakeSituationServiceDto.of(LocalDate.now());
        GetTakeSituationResponse response = adminTakeService.getTakeSituation(dto);
        return ResponseEntity.ok(response);
    }


    /* 차량 반납 보고서 상세 조회 API */
    @Operation(summary = "차량 반납 보고서 상세 조회 API", description = "차량 반납 보고서를 조회합니다.")
    @GetMapping("/reports/{winningId}/return")
    public ResponseEntity<GetReturnReportResponse> getTakeReport(@PathVariable Long winningId) {
        GetReturnReportResponse response = adminTakeService.getReturnReport(GetReturnReportServiceDto.of(winningId));
        return ResponseEntity.ok(response);
    }
}