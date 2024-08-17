package kea.enter.enterbe.api.take.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kea.enter.enterbe.api.question.controller.dto.response.GetAnswerResponseDto;
import kea.enter.enterbe.api.take.controller.dto.request.GetReportListRequest;
import kea.enter.enterbe.api.take.controller.dto.response.GetReportListResponse;
import kea.enter.enterbe.api.take.controller.dto.response.GetTakeReportResponse;
import kea.enter.enterbe.api.take.controller.dto.response.GetTakeSituationResponse;
import kea.enter.enterbe.api.take.service.dto.GetTakeReportServiceDto;
import kea.enter.enterbe.api.take.controller.dto.response.GetReturnReportResponse;
import kea.enter.enterbe.api.take.service.AdminTakeService;
import kea.enter.enterbe.api.take.service.dto.GetReturnReportServiceDto;
import kea.enter.enterbe.api.take.service.dto.GetTakeSituationServiceDto;
import kea.enter.enterbe.global.common.exception.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;

@Tag(name = "[관리자] 인수 관련 API", description = "Take")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/takes")
public class AdminTakeController {
    private final AdminTakeService adminTakeService;

    /* 인수 현황 조회 API */
    @Operation(summary = "인수 현황 조회 API", description = "이전 회차 인수 현황을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetTakeSituationResponse.class))),
        @ApiResponse(responseCode = "AUT-ERR-010", description = "인증되지 않은 사용자입니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "GLB-ERR-003", description = "내부 서버 오류입니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("")
    public ResponseEntity<GetTakeSituationResponse> getTakeSituation() {
        GetTakeSituationServiceDto dto = GetTakeSituationServiceDto.of(LocalDate.now());
        GetTakeSituationResponse response = adminTakeService.getTakeSituation(dto);
        return ResponseEntity.ok(response);
    }

    /* 인수 관리 목록 조회 API */
    @Operation(summary = "인수 관리 목록 조회 API", description = "인수 관리 목록을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetReportListResponse.class))),
        @ApiResponse(responseCode = "AUT-ERR-010", description = "인증되지 않은 사용자입니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "GLB-ERR-003", description = "내부 서버 오류입니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/reports")
    public ResponseEntity<GetReportListResponse> getTakeReport(
        @Valid @ParameterObject GetReportListRequest request,
        @ParameterObject Pageable pageable) {
        GetReportListResponse response = adminTakeService.getReportList(request.toService(pageable));
        return ResponseEntity.ok(response);
    }
  
    /* 차량 인수 보고서 상세 조회 API */
    @Operation(summary = "차량 인수 보고서 상세 조회 API", description = "차량 인수 보고서를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetTakeReportResponse.class))),
        @ApiResponse(responseCode = "AUT-ERR-010", description = "인증되지 않은 사용자입니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "VHR-ERR-001", description = "차량 보고서를 찾을 수 없습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "GLB-ERR-003", description = "내부 서버 오류입니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/reports/take")
    public ResponseEntity<GetTakeReportResponse> getTakeReport(@RequestParam Long winningId) {
        GetTakeReportResponse response = adminTakeService.getTakeReport(GetTakeReportServiceDto.of(winningId));
        return ResponseEntity.ok(response);
    }

    /* 차량 반납 보고서 상세 조회 API */
    @Operation(summary = "차량 반납 보고서 상세 조회 API", description = "차량 반납 보고서를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetReturnReportResponse.class))),
        @ApiResponse(responseCode = "AUT-ERR-010", description = "인증되지 않은 사용자입니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "VHR-ERR-001", description = "차량 보고서를 찾을 수 없습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "GLB-ERR-003", description = "내부 서버 오류입니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/reports/return")
    public ResponseEntity<GetReturnReportResponse> getReturnReport(@RequestParam Long winningId) {
        GetReturnReportResponse response = adminTakeService.getReturnReport(GetReturnReportServiceDto.of(winningId));
        return ResponseEntity.ok(response);
    }

}