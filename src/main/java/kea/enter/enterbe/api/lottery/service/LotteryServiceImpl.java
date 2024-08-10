package kea.enter.enterbe.api.lottery.service;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import kea.enter.enterbe.api.lottery.controller.dto.response.GetLotteryResponse;
import kea.enter.enterbe.api.lottery.controller.dto.response.GetRecentCompetitionRateResponse;
import kea.enter.enterbe.api.lottery.controller.dto.response.GetRecentWaitingAverageNumbersResponse;
import kea.enter.enterbe.api.lottery.service.dto.GetLotteryServiceDto;
import kea.enter.enterbe.domain.apply.entity.ApplyRound;
import kea.enter.enterbe.domain.apply.entity.ApplyRoundState;
import kea.enter.enterbe.domain.apply.entity.ApplyState;
import kea.enter.enterbe.domain.apply.repository.ApplyRepository;
import kea.enter.enterbe.domain.apply.repository.ApplyRoundRepository;
import kea.enter.enterbe.domain.lottery.entity.WaitingState;
import kea.enter.enterbe.domain.lottery.entity.Winning;
import kea.enter.enterbe.domain.lottery.entity.WinningState;
import kea.enter.enterbe.domain.lottery.repository.WaitingRepository;
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
    private final WaitingRepository waitingRepository;
    private final Clock clock;

    @Override
    public List<GetRecentCompetitionRateResponse> getRecentCompetitionRate() {
        List<ApplyRound> thisWeekApplyRound = getApplyRoundByThisWeek();
        if (thisWeekApplyRound.isEmpty()) {
            return new ArrayList<>();
        }
        // 한 일주일에 관해서는 회차가 같다.
        int round = thisWeekApplyRound.get(0).getRound();
        List<GetRecentCompetitionRateResponse> list = new ArrayList<>();
        Integer number;
        Integer winner;
        //최근 5회차
        for (int i = round; i > round - 5; i--) {
            if (i < 1) {
                break;
            }
            number = applyRepository.countByApplyRoundRoundAndStateAndApplyRoundState(i,
                ApplyState.ACTIVE, ApplyRoundState.ACTIVE);
            winner = winningRepository.countByApplyApplyRoundRoundAndState(i,
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

    @Override
    public List<GetRecentWaitingAverageNumbersResponse> getAverageWaitingNumbers() {
        List<GetRecentWaitingAverageNumbersResponse> list = new ArrayList<>();
        List<ApplyRound> thisWeekApplyRound = getApplyRoundByThisWeek();
        if (thisWeekApplyRound.isEmpty()) {
            return new ArrayList<>();
        }
        // 한 일주일에 관해서는 회차가 같다.
        int round = thisWeekApplyRound.get(0).getRound();
        //최근 5회차
        List<Winning> winnings;
        int sum;
        for (int i = round; i > round - 5; i--) {
            // 회차가 1미만이 될경우 종료
            if (i < 1) {
                break;
            }
            //라운드의 모든 당첨자 조회
            winnings = winningRepository.findAllByApplyApplyRoundRoundAndState(
                round, WinningState.ACTIVE);
            sum = 0;
            for (Winning winning : winnings) {
                //당첨자의 대기번호 더하기
                sum += waitingRepository.findWaitingNoByApplyIdAndState(
                    winning.getApply().getId(), WaitingState.ACTIVE);
            }
            //더한 대기번호 평균
            double result = (double) sum / winnings.size();
            list.add(GetRecentWaitingAverageNumbersResponse.of(i, String.format("%.2f", result)));
        }
        return list;
    }

    public List<GetLotteryResponse> getLotteryList(GetLotteryServiceDto dto) {
        return applyRepository.findAllLotteryResponsebyId(dto.getMemberId());
    }

    private List<ApplyRound> getApplyRoundByThisWeek() {
        // 오늘을 기준으로 이번주 기간을 구한다
        // 이번주에 인수 반납하는 회차이면 이미 당첨자가 나온 상황이므로 이번회차부터 5회차 확인
        LocalDate today = LocalDate.now(clock);
        LocalDate thisMonday = today.with(DayOfWeek.MONDAY);  // 이번주 월요일
        LocalDate thisSunday = today.with(DayOfWeek.SUNDAY);  // 이번주 일요일
        return applyRoundRepository.findAllApplyRoundsByTakeDateBetweenAndState(
            thisMonday, thisSunday, ApplyRoundState.ACTIVE);
    }
}
