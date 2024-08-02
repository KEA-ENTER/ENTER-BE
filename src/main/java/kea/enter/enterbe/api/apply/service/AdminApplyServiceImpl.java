package kea.enter.enterbe.api.apply.service;

import kea.enter.enterbe.api.apply.controller.response.GetApplySituationResponse;
import kea.enter.enterbe.api.apply.service.dto.GetApplySituationServiceDto;
import kea.enter.enterbe.domain.apply.entity.Apply;
import kea.enter.enterbe.domain.apply.entity.ApplyState;
import kea.enter.enterbe.domain.apply.repository.ApplyRepository;
import kea.enter.enterbe.domain.apply.entity.ApplyRound;
import kea.enter.enterbe.domain.apply.entity.ApplyRoundState;
import kea.enter.enterbe.domain.apply.repository.ApplyRoundRepository;
import kea.enter.enterbe.domain.lottery.entity.Winning;
import kea.enter.enterbe.domain.lottery.entity.WinningState;
import kea.enter.enterbe.domain.lottery.repository.WinningRepository;
import kea.enter.enterbe.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static kea.enter.enterbe.global.common.exception.ResponseCode.APPLY_ROUND_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminApplyServiceImpl implements AdminApplyService {
    private final ApplyRoundRepository applyRoundRepository;
    private final ApplyRepository applyRepository;
    private final WinningRepository winningRepository;

    /* 응모 현황 조회 API */
    public GetApplySituationResponse getApplySituation(GetApplySituationServiceDto dto) {
        // 오늘을 기준으로 오늘이 몇요일인지 구한다
        LocalDate today = dto.getToday();
        LocalDate thisMonday = today.with(DayOfWeek.MONDAY);  // 이번주 월요일
        LocalDate thisSunday = thisMonday.plusDays(6);  // 이번주 일요일

        // 신청회차의 take_date가 오늘 이후부터 이번주 일요일 사이라면 이번주 회차라고 간주한다
        int applyCnt = 0, winningCnt = 0, cancelCnt = 0;
        List<ApplyRound> applyRoundList = findApplyRoundsByTakeDateBetween(thisMonday, thisSunday);

        if(applyRoundList.isEmpty()) {
            throw new CustomException(APPLY_ROUND_NOT_FOUND);
        }

        // 이번주의 모든 ApplyRound에 대한 신청자수, 당첨자수, 취소자수를 더한다
        for(ApplyRound applyRound : applyRoundList) {
            List<Apply> applyList = applyRepository.findAllByApplyRoundIdAndState(applyRound.getId(), ApplyState.ACTIVE);
            applyCnt += applyList.size();

            List<Winning> winningList = winningRepository.findAllByApplyApplyRoundIdAndState(applyRound.getId(), WinningState.ACTIVE);
            winningCnt += winningList.size();

            List<Winning> cancelList = winningRepository.findAllByApplyApplyRoundIdAndState(applyRound.getId(), WinningState.INACTIVE);
            cancelCnt += cancelList.size();
        }

        return GetApplySituationResponse.of(applyRoundList.get(0).getApplyRound(), applyCnt, winningCnt, cancelCnt);
    }

    public List<ApplyRound> findApplyRoundsByTakeDateBetween(LocalDate startDate, LocalDate endDate) {
        return applyRoundRepository.findAllByTakeDateBetweenAndState(startDate, endDate, ApplyRoundState.ACTIVE);
    }

}
