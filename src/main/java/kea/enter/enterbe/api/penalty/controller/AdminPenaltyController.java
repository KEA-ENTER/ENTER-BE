package kea.enter.enterbe.api.penalty.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kea.enter.enterbe.api.penalty.controller.request.PenaltyRequest;
import kea.enter.enterbe.api.penalty.service.AdminPenaltyServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static kea.enter.enterbe.global.common.api.CustomResponseCode.SUCCESS;

@Tag(name = "페널티", description = "[관리자] Penalty")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/penalties")
public class AdminPenaltyController {
    private final AdminPenaltyServiceImpl adminPenaltyService;

    /* 페널티 부여 API */
    @Operation(summary = "사용자에게 페널티를 부여합니다")
    @PostMapping("/{memberId}")
    public ResponseEntity<String> createPenalty(
        @PathVariable Long memberId,
        @Valid @RequestBody PenaltyRequest penaltyRequest) {
        // TODO: 어드민 권한 검사, level 추가
        adminPenaltyService.createPenalty(memberId, penaltyRequest.toService());
        return ResponseEntity.ok(SUCCESS.getMessage());
    }
}