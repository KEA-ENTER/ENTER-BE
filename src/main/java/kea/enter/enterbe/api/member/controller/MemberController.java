package kea.enter.enterbe.api.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kea.enter.enterbe.api.member.controller.dto.response.GetMemberScoreResponse;
import kea.enter.enterbe.api.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<GetMemberScoreResponse> getMemberScorePercent() {
        //todo: spring security 구현 완료 시 token에서 memberId 값 가져오기
        Long memberId = 1L;
        return ResponseEntity.ok(memberService.getMemberScorePercent(memberId));
    }
}
