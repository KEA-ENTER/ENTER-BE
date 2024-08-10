package kea.enter.enterbe.api.lottery.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kea.enter.enterbe.api.lottery.controller.dto.response.GetLotteryResponse;
import kea.enter.enterbe.api.lottery.controller.dto.response.GetRecentCompetitionRateResponse;
import kea.enter.enterbe.api.lottery.controller.dto.response.GetRecentWaitingAverageNumbersResponse;
import kea.enter.enterbe.api.lottery.service.LotteryService;
import kea.enter.enterbe.api.lottery.service.dto.GetLotteryServiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "추첨", description = "추첨 API 명세서")
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

    @Operation(summary = "사용자의 추첨 참여 내역 조회 API",
        parameters = {
        @Parameter(name = "Authorization", description = "Bearer Token", required = true,
            in = ParameterIn.HEADER, schema = @Schema(type = "string"))})
    @GetMapping("")
    public ResponseEntity<List<GetLotteryResponse>> getLotteryList(
        Authentication authentication) {
        return ResponseEntity.ok(lotteryService.getLotteryList(GetLotteryServiceDto.of(Long.valueOf(authentication.getName()))));
    }
}
