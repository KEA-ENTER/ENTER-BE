package kea.enter.enterbe.api.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kea.enter.enterbe.api.member.controller.dto.response.GetMemberPostReportTypeResponse;
import kea.enter.enterbe.api.member.controller.dto.response.GetMemberScoreResponse;
import kea.enter.enterbe.api.member.controller.dto.response.GetRoutingResponse;
import kea.enter.enterbe.api.member.service.MemberService;
import kea.enter.enterbe.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "[사용자] 사용자 관련 API", description = "Member")
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "가중치 조회")
    @GetMapping(value = "/score")
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

    @Operation(
        summary = "사용자 라우팅 정보 조회 (차량 신청 메뉴에 접근했을 때 라우팅)",
        description = "로그인이 성공한 사용자의 상태(사원, 신청자, 당첨자, 대기자, 미당첨자)와 기간(신청서 작성 기간, 추첨 대기 기간, 결과 발표 기간, 회차 대기 기간)에 따른 "
            + "페이지 정보와 사용자의 상태를 제공하는 API"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "라우팅 페이지 정보와 사용자의 상태 값",
            content = @Content(schema = @Schema(implementation = GetRoutingResponse.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "올바르지 않은 시간 값이 들어온 경우", content = @Content(schema = @Schema(implementation = CustomException.class))),
        @ApiResponse(responseCode = "404", description = "라우팅 정보를 제공할 수 없는 경우", content = @Content(schema = @Schema(implementation = CustomException.class)))
    })
    @GetMapping(value = "/routing")
    public ResponseEntity<GetRoutingResponse> getUserRoutingInformation(Authentication authentication) {
        Long memberId = Long.valueOf(authentication.getName());
        return ResponseEntity.ok(memberService.getRoutingInformation(memberId));
    }
}

