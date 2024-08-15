package kea.enter.enterbe.api.lottery.service;

import java.util.List;
import kea.enter.enterbe.api.lottery.controller.dto.response.GetLotteryResponse;
import kea.enter.enterbe.api.lottery.controller.dto.response.GetLotteryResultResponse;
import kea.enter.enterbe.api.lottery.controller.dto.response.GetRecentCompetitionRateResponse;
import kea.enter.enterbe.api.lottery.controller.dto.response.GetRecentWaitingAverageNumbersResponse;
import kea.enter.enterbe.api.lottery.service.dto.GetLotteryResultServiceDto;
import kea.enter.enterbe.api.lottery.service.dto.GetLotteryServiceDto;
import org.springframework.http.ResponseEntity;

public interface LotteryService {

    List<GetRecentCompetitionRateResponse> getRecentCompetitionRate();

    List<GetRecentWaitingAverageNumbersResponse> getAverageWaitingNumbers();

    GetLotteryResultResponse getLottery(GetLotteryResultServiceDto dto);
    GetLotteryResponse getLotteryList(GetLotteryServiceDto dto);
}
