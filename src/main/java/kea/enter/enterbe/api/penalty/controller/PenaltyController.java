package kea.enter.enterbe.api.penalty.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kea.enter.enterbe.api.penalty.controller.dto.response.GetMemberInPenaltyPeriodResponse;
import kea.enter.enterbe.api.penalty.controller.dto.response.GetPenaltyListResponse;
import kea.enter.enterbe.api.penalty.controller.dto.response.GetPenaltyResponse;
import kea.enter.enterbe.api.penalty.service.PenaltyService;
import kea.enter.enterbe.api.penalty.service.dto.GetPenaltyListServiceDto;
import kea.enter.enterbe.api.penalty.service.dto.GetPenaltyServiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "[사용자] 페널티 조회 API", description = "Penalty")
@RestController
@RequiredArgsConstructor
@RequestMapping("/penalties")
public class PenaltyController {
    private final PenaltyService penaltyService;

    @Operation(summary = "패널티 목록 조회 API")
    @GetMapping("")
    public GetPenaltyListResponse getPenaltyList(
        @RequestParam int page,
        Authentication authentication) {

        PageRequest pageRequest = PageRequest.of(page, 10);

        return penaltyService.getPenaltyList(
            GetPenaltyListServiceDto.of(
                Long.valueOf(authentication.getName()),
                pageRequest));
    }

    @Operation(summary = "패널티 상세 정보 조회 API")
    @GetMapping("/{penaltyId}")
    public GetPenaltyResponse getPenalty(
        Authentication authentication,
        @PathVariable Long penaltyId) {

        return penaltyService.getPenalty(GetPenaltyServiceDto.of(Long.valueOf(authentication.getName()), penaltyId));
    }

    @Operation(summary = "패널티로 인해 제한 기간에 접속한 사용자인지 확인")
    @GetMapping("/in-progress")
    public ResponseEntity<GetMemberInPenaltyPeriodResponse> getMemberPenalties(Authentication authentication) {
        Long memberId = Long.valueOf(authentication.getName());
        return ResponseEntity.ok(penaltyService.getMemberInPenaltyPeriod(memberId));
    }
}
