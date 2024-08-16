package kea.enter.enterbe.api.penalty.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kea.enter.enterbe.api.lottery.controller.dto.response.GetLotteryListResponse;
import kea.enter.enterbe.api.penalty.controller.dto.request.PostAdminPenaltyRequest;
import kea.enter.enterbe.api.penalty.controller.dto.response.GetAdminPenaltyListResponse;
import kea.enter.enterbe.api.penalty.service.AdminPenaltyService;
import kea.enter.enterbe.api.penalty.service.dto.DeleteAdminPenaltyServiceDto;
import kea.enter.enterbe.api.penalty.service.dto.GetAdminPenaltyListServiceDto;
import kea.enter.enterbe.global.common.exception.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static kea.enter.enterbe.global.common.api.CustomResponseCode.SUCCESS;

@Tag(name = "[관리자] 페널티 관련 API", description = "Penalty")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/penalties")
public class AdminPenaltyController {
    private final AdminPenaltyService adminPenaltyService;

    /* 페널티 부여 API */
    @Operation(summary = "사용자 페널티 부여 API", description = "사용자에게 페널티를 부여합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "AUT-ERR-010", description = "인증되지 않은 사용자입니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "MEM-ERR-001", description = "멤버를 찾을 수 없습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "GLB-ERR-003", description = "내부 서버 오류입니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/members/{memberId}")
    public ResponseEntity<String> createPenalty(
        @PathVariable Long memberId,
        @Valid @RequestBody PostAdminPenaltyRequest postAdminPenaltyRequest) {
        adminPenaltyService.createPenalty(postAdminPenaltyRequest.toService(memberId));
        return ResponseEntity.ok(SUCCESS.getMessage());
    }

    /* 페널티 목록 조회 API */
    @Operation(summary = "사용자 페널티 목록 조회 API", description = "사용자의 페널티 목록을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = GetAdminPenaltyListResponse.class))),
        @ApiResponse(responseCode = "AUT-ERR-010", description = "인증되지 않은 사용자입니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "MEM-ERR-001", description = "멤버를 찾을 수 없습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "GLB-ERR-003", description = "내부 서버 오류입니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/members/{memberId}")
    public ResponseEntity<List<GetAdminPenaltyListResponse>> getPenaltyList(@PathVariable Long memberId) {
        List<GetAdminPenaltyListResponse> response =  adminPenaltyService.getPenaltyList(GetAdminPenaltyListServiceDto.of(memberId));
        return ResponseEntity.ok(response);
    }

    /* 페널티 삭제 API */
    @Operation(summary = "사용자 페널티 삭제 API", description = "사용자의 페널티를 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "AUT-ERR-010", description = "인증되지 않은 사용자입니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "MEM-ERR-001", description = "멤버를 찾을 수 없습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "PEN-ERR-001", description = "페널티를 찾을 수 없습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "GLB-ERR-003", description = "내부 서버 오류입니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/members/{memberId}/{penaltyId}")
    public ResponseEntity<String> getPenaltyList(@PathVariable Long memberId, @PathVariable Long penaltyId) {
        adminPenaltyService.deletePenalty(DeleteAdminPenaltyServiceDto.of(memberId, penaltyId));
        return ResponseEntity.ok(SUCCESS.getMessage());
    }
}