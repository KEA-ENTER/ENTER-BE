package kea.enter.enterbe.api.lottery.service;

import java.util.List;
import kea.enter.enterbe.api.lottery.controller.dto.response.GetRecentCompetitionRateResponse;
import org.springframework.http.ResponseEntity;

public interface LotteryService {

    List<GetRecentCompetitionRateResponse> getRecentCompetitionRate();
}
