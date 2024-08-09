package kea.enter.enterbe.api.lottery.service;

import java.util.List;
import kea.enter.enterbe.api.lottery.controller.dto.response.GetLotteryResponse;
import kea.enter.enterbe.api.lottery.controller.dto.response.GetRecentCompetitionRateResponse;
import kea.enter.enterbe.api.lottery.controller.dto.response.GetRecentWaitingAverageNumbersResponse;
import org.springframework.http.ResponseEntity;

public interface LotteryService {

    List<GetRecentCompetitionRateResponse> getRecentCompetitionRate();

    List<GetRecentWaitingAverageNumbersResponse> getAverageWaitingNumbers();
    GetLotteryResponse getLottery();
}
