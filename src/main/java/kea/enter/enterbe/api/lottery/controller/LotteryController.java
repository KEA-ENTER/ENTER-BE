package kea.enter.enterbe.api.lottery.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kea.enter.enterbe.api.lottery.controller.dto.response.GetLotteryResponse;
import kea.enter.enterbe.api.lottery.controller.dto.response.GetLotteryResultResponse;
import kea.enter.enterbe.api.lottery.controller.dto.response.GetRecentCompetitionRateResponse;
import kea.enter.enterbe.api.lottery.controller.dto.response.GetRecentWaitingAverageNumbersResponse;
import kea.enter.enterbe.api.lottery.service.LotteryService;
import kea.enter.enterbe.api.lottery.service.dto.GetLotteryResultServiceDto;
import kea.enter.enterbe.api.lottery.service.dto.GetLotteryServiceDto;
import kea.enter.enterbe.api.penalty.controller.dto.response.GetPenaltyListResponse;
import kea.enter.enterbe.api.penalty.service.dto.GetPenaltyListServiceDto;
import kea.enter.enterbe.global.common.exception.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "[사용자] 추첨 관련 API", description = "Lottery")
@RestController
@RequiredArgsConstructor
@RequestMapping("/lotteries")
public class LotteryController {

    private final LotteryService lotteryService;

    @Operation(summary = "최근 5회차의 회차별 평균 경쟁률")
    @GetMapping("competition-rate")
    public ResponseEntity<List<GetRecentCompetitionRateResponse>> getRecentCompetitionRate() {
        return ResponseEntity.ok(lotteryService.getRecentCompetitionRate());
    }

    @Operation(summary = "최근 5회차의 회차별 평균 대기번호 빠짐 수")
    @GetMapping("average-waiting-numbers")
    public ResponseEntity<List<GetRecentWaitingAverageNumbersResponse>> getAverageWaitingNumbers() {
        return ResponseEntity.ok(lotteryService.getAverageWaitingNumbers());
    }
    @Operation(summary = "당첨 여부 조회")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = GetLotteryResultResponse.class),
            examples = {
                @ExampleObject(name = "당첨", value = "{\"winning\": true, \"waitingNumber\": null}"),
                @ExampleObject(name = "대기", value = "{\"winning\": false, \"waitingNumber\": 5}"),
                @ExampleObject(name = "탈락", value = "{\"winning\": false, \"waitingNumber\": null}")
            }
        ))
    })
    @GetMapping("result")
    public ResponseEntity<GetLotteryResultResponse> getLottery(Authentication authentication) {
        Long memberId = Long.valueOf(authentication.getName());
        return ResponseEntity.ok(lotteryService.getLottery(GetLotteryResultServiceDto.of(memberId)));
    }
    @Operation(summary = "사용자의 추첨 참여 내역 조회 API",
        parameters = {
        @Parameter(name = "Authorization", description = "Bearer Token", required = true,
            in = ParameterIn.HEADER, schema = @Schema(type = "string"))})
    @GetMapping("")
    public GetLotteryResponse getLotteryList(
        @PageableDefault(size = 10) Pageable pageable,
        Authentication authentication) {

        return lotteryService.getLotteryList(
            GetLotteryServiceDto.of(
                Long.valueOf(authentication.getName()),
                pageable));
    }
}
