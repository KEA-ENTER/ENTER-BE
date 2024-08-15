package kea.enter.enterbe.global.algorithm;

import io.swagger.v3.oas.annotations.Operation;
import kea.enter.enterbe.global.quartz.job.CalculateWeight;
import kea.enter.enterbe.global.quartz.job.ProcessingLottery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestC {

        private final CalculateWeight calculateWeight;
        private final ProcessingLottery processingLottery;

        @Operation(summary = "가중치 갱신", description = "전체 멤버에 대한 가중치 갱신 수행 (기존 스케줄러 작업)")
        @GetMapping("/weight")
        public ResponseEntity<CalculateDto> weight() {
            return ResponseEntity.ok(calculateWeight.calculateWeight());
        }

        @Operation(summary = "당첨자 추첨", description = "신청자들을 당첨자, 대기자로 추첨하여 저장")
        @GetMapping("/lottery")
        public ResponseEntity<LotteryDto> lottery() {
            return ResponseEntity.ok(processingLottery.processLottery());
        }


}
