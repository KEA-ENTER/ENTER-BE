package kea.enter.enterbe.api.apply.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import kea.enter.enterbe.api.apply.controller.dto.response.GetApplyDetailResponse;
import kea.enter.enterbe.api.apply.controller.dto.response.GetApplyResponse;
import kea.enter.enterbe.api.apply.controller.dto.response.GetApplyVehicleResponse;
import kea.enter.enterbe.api.apply.service.ApplyService;
import kea.enter.enterbe.api.apply.service.dto.GetApplyDetailServiceDto;
import kea.enter.enterbe.api.apply.service.dto.GetApplyServiceDto;
import kea.enter.enterbe.api.apply.service.dto.GetApplyVehicleServiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "신청", description = "사용자 신청 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/applies")
public class ApplyController {

    private final ApplyService applyService;

    @Operation(summary = "신청 가능 날짜 조회 API", description = "신청 가능 날짜를 조회합니다.")
    @GetMapping("")
    public ResponseEntity<List<GetApplyResponse>> getApply() {
        GetApplyServiceDto dto = GetApplyServiceDto.of(LocalDate.now());
        List<GetApplyResponse> response = applyService.getApply(dto);
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "신청 가능한 차량 목록 조회 API", description = "신청 가능한 차량을 모두 조회합니다.")
    @GetMapping("/vehicles")
    public ResponseEntity<List<GetApplyVehicleResponse>> getApplyVehicles(
        @RequestParam LocalDate takeDate,
        @RequestParam LocalDate returnDate) {
        GetApplyVehicleServiceDto dto = GetApplyVehicleServiceDto.of(takeDate, returnDate);
        List<GetApplyVehicleResponse> response = applyService.getApplyVehicles(dto);
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "차량 신청 내역 조회 API", description = "해당 사용자의 현재 회차의 차량 신청 내역을 조회합니다.")
    @GetMapping("/detail")
    public ResponseEntity<GetApplyDetailResponse> getApplyDetail(Authentication authentication) {
        Long memberId = Long.valueOf(authentication.getUsername());
        GetApplyDetailServiceDto dto = GetApplyDetailServiceDto.of(memberId);
        GetApplyDetailResponse response = applyService.getApplyDetail(dto);
        return ResponseEntity.ok(response);
    }
}