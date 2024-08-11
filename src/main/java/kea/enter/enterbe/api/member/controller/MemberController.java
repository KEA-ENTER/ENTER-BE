package kea.enter.enterbe.api.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kea.enter.enterbe.api.member.controller.dto.response.GetMemberPostReportTypeResponse;
import kea.enter.enterbe.api.member.controller.dto.response.GetMemberScoreResponse;
import kea.enter.enterbe.api.member.controller.dto.response.GetRoutingResponse;
import kea.enter.enterbe.api.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "유저", description = "유저 API 명세서")
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "가중치 조회")
    @PostMapping(value = "/score")
    public ResponseEntity<GetMemberScoreResponse> getMemberScorePercent(
        Authentication authentication) {
        Long memberId = Long.valueOf(authentication.getName());
        return ResponseEntity.ok(memberService.getMemberScorePercent(memberId));
    }

    @Operation(summary = "사용자 어떤 보고서 제출해야하는지 조회")
    @GetMapping(value = "/report/post-type")
    public ResponseEntity<GetMemberPostReportTypeResponse> getMemberPostReportType(Authentication authentication) {
        Long memberId = Long.valueOf(authentication.getName());
        return ResponseEntity.ok(memberService.getMemberPostReportType(memberId));
    }

    @Operation(summary = "사용자 라우팅 정보 조회 (차량 신청 메뉴에 접근했을 때 라우팅)")
    @GetMapping(value = "/routing")
    public ResponseEntity<GetRoutingResponse> getUserRoutingInformation(Authentication authentication) {
        Long memberId = Long.valueOf(authentication.getName());
        return ResponseEntity.ok(memberService.getRoutingInformation(memberId));
    }
}

