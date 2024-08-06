package kea.enter.enterbe.api.lottery.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kea.enter.enterbe.api.lottery.controller.dto.response.GetRecentCompetitionRateResponse;
import kea.enter.enterbe.api.lottery.service.LotteryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    @GetMapping
    public ResponseEntity<List<GetRecentCompetitionRateResponse>> getRecentCompetitionRate() {
        return ResponseEntity.ok(lotteryService.getRecentCompetitionRate());
    }
}
