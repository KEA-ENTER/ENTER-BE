package kea.enter.enterbe.api.penalty.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kea.enter.enterbe.api.penalty.controller.request.PostPenaltyRequest;
import kea.enter.enterbe.api.penalty.controller.response.GetPenaltyListResponse;
import kea.enter.enterbe.api.penalty.service.AdminPenaltyService;
import kea.enter.enterbe.api.penalty.service.dto.DeletePenaltyServiceDto;
import kea.enter.enterbe.api.penalty.service.dto.GetPenaltyListServiceDto;
import lombok.RequiredArgsConstructor;
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

@Tag(name = "페널티", description = "[관리자] Penalty")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/penalties")
public class AdminPenaltyController {
    private final AdminPenaltyService adminPenaltyService;

    /* 페널티 부여 API */
    @Operation(summary = "사용자 페널티 부여 API", description = "사용자에게 페널티를 부여합니다.")
    @PostMapping("/members/{memberId}")
    public ResponseEntity<String> createPenalty(
        @PathVariable Long memberId,
        @Valid @RequestBody PostPenaltyRequest postPenaltyRequest) {
        // TODO: 어드민 권한 검사
        adminPenaltyService.createPenalty(postPenaltyRequest.toService(memberId));
        return ResponseEntity.ok(SUCCESS.getMessage());
    }

    /* 페널티 목록 조회 API */
    @Operation(summary = "사용자 페널티 목록 조회 API", description = "사용자의 페널티 목록을 조회합니다.")
    @GetMapping("/members/{memberId}")
    public ResponseEntity<List<GetPenaltyListResponse>> getPenaltyList(@PathVariable Long memberId) {
        // TODO: 어드민 권한 검사
        List<GetPenaltyListResponse> response =  adminPenaltyService.getPenaltyList(GetPenaltyListServiceDto.of(memberId));
        return ResponseEntity.ok(response);
    }

    /* 페널티 삭제 API */
    @Operation(summary = "사용자 페널티 삭제 API", description = "사용자의 페널티를 삭제합니다.")
    @DeleteMapping("/members/{memberId}/{penaltyId}")
    public ResponseEntity<String> getPenaltyList(@PathVariable Long memberId, @PathVariable Long penaltyId) {
        // TODO: 어드민 권한 검사
        adminPenaltyService.deletePenalty(DeletePenaltyServiceDto.of(memberId, penaltyId));
        return ResponseEntity.ok(SUCCESS.getMessage());
    }
}