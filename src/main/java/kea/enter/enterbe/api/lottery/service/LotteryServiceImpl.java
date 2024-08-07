package kea.enter.enterbe.api.lottery.service;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import kea.enter.enterbe.api.lottery.controller.dto.response.GetRecentCompetitionRateResponse;
import kea.enter.enterbe.domain.apply.entity.ApplyRound;
import kea.enter.enterbe.domain.apply.entity.ApplyRoundState;
import kea.enter.enterbe.domain.apply.entity.ApplyState;
import kea.enter.enterbe.domain.apply.repository.ApplyRepository;
import kea.enter.enterbe.domain.apply.repository.ApplyRoundRepository;
import kea.enter.enterbe.domain.lottery.entity.WinningState;
import kea.enter.enterbe.domain.lottery.repository.WinningRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LotteryServiceImpl implements LotteryService {

    private final ApplyRoundRepository applyRoundRepository;
    private final ApplyRepository applyRepository;
    private final WinningRepository winningRepository;
    private final Clock clock;

    @Override
    public List<GetRecentCompetitionRateResponse> getRecentCompetitionRate() {
        // 오늘을 기준으로 이번주 기간을 구한다
        // 이번주에 인수 반납하는 회차이면 이미 당첨자가 나온 상황이므로 이번회차부터 5회차 확인
        LocalDate today = LocalDate.now(clock);
        LocalDate thisMonday = today.with(DayOfWeek.MONDAY);  // 이번주 월요일
        LocalDate thisSunday = today.with(DayOfWeek.SUNDAY);  // 이번주 일요일
        // 한 일주일에 관해서는 회차가 같다.
        List<ApplyRound> thisApplyRound = applyRoundRepository.findAllApplyRoundsByTakeDateBetweenAndState(
            thisMonday, thisSunday, ApplyRoundState.ACTIVE);
        if (thisApplyRound.isEmpty()) {
            return new ArrayList<>();
        }
        int round = thisApplyRound.get(0).getApplyRound();
        List<GetRecentCompetitionRateResponse> list = new ArrayList<>();
        Integer number;
        Integer winner;
        for (int i = round; i > round - 5; i--) {
            if (i < 1) {
                break;
            }
            number = applyRepository.countByApplyRoundApplyRoundAndStateAndApplyRoundState(i,
                ApplyState.ACTIVE, ApplyRoundState.ACTIVE);
            winner = winningRepository.countByApplyApplyRoundApplyRoundAndState(i,
                WinningState.ACTIVE);
            double result = (double) number / winner;
            if (winner == 0) {
                result = 0;
            }
            list.add(GetRecentCompetitionRateResponse.of(
                i, String.format("%.2f", result)
            ));
        }
        return list;
    }
}
