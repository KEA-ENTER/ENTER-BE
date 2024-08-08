package kea.enter.enterbe.api.take.service;

import kea.enter.enterbe.api.take.controller.dto.response.GetTakeSituationResponse;
import kea.enter.enterbe.api.take.service.dto.GetTakeSituationServiceDto;
import kea.enter.enterbe.domain.apply.entity.Apply;
import kea.enter.enterbe.domain.apply.entity.ApplyRound;
import kea.enter.enterbe.domain.apply.entity.ApplyRoundState;
import kea.enter.enterbe.domain.apply.entity.ApplyState;
import kea.enter.enterbe.domain.apply.repository.ApplyRepository;
import kea.enter.enterbe.domain.apply.repository.ApplyRoundRepository;
import kea.enter.enterbe.domain.lottery.entity.Winning;
import kea.enter.enterbe.domain.lottery.entity.WinningState;
import kea.enter.enterbe.domain.lottery.repository.WinningRepository;
import kea.enter.enterbe.domain.take.entity.VehicleReport;
import kea.enter.enterbe.domain.take.entity.VehicleReportState;
import kea.enter.enterbe.domain.take.entity.VehicleReportType;
import kea.enter.enterbe.domain.take.repository.VehicleReportRepository;
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
public class AdminTakeServiceImpl implements AdminTakeService {
    private final ApplyRoundRepository applyRoundRepository;
    private final ApplyRepository applyRepository;
    private final WinningRepository winningRepository;
    private final VehicleReportRepository vehicleReportRepository;

    /* 인수 현황 조회 API */
    public GetTakeSituationResponse getTakeSituation(GetTakeSituationServiceDto dto) {
        // 오늘을 기준으로 저번주 기간을 구한다
        LocalDate date = dto.getToday().minusDays(7);
        LocalDate lastMonday = date.with(DayOfWeek.MONDAY);  // 저번주 월요일
        LocalDate lastSunday = date.with(DayOfWeek.SUNDAY);  // 저번주 일요일

        // 신청회차의 take_date가 저번주 월요일부터 저번주 일요일 사이라면 저번주 회차라고 간주한다
        int applyCnt = 0, takeCnt = 0, noShowCnt = 0;
        List<ApplyRound> applyRoundList = findApplyRoundsByTakeDateBetween(lastMonday, lastSunday);

        if(applyRoundList.isEmpty()) {
            throw new CustomException(APPLY_ROUND_NOT_FOUND);
        }

        // 저번주의 모든 ApplyRound에 대한 신청자수, 당첨자수, 취소자수를 더한다
        for(ApplyRound applyRound : applyRoundList) {
            List<Apply> applyList = applyRepository.findAllByApplyRoundIdAndState(applyRound.getId(), ApplyState.ACTIVE);
            applyCnt += applyList.size();

            List<Winning> winningList = winningRepository.findAllByApplyApplyRoundIdAndState(applyRound.getId(), WinningState.ACTIVE);
            int curWinningCnt = winningList.size();

            List<VehicleReport> vehicleReportList = vehicleReportRepository.findAllByWinningApplyApplyRoundIdAndTypeAndState(applyRound.getId(), VehicleReportType.TAKE, VehicleReportState.ACTIVE);
            int curTakeCnt = vehicleReportList.size();
            int curNoShowCnt = curWinningCnt - curTakeCnt;

            takeCnt += curTakeCnt;
            noShowCnt += curNoShowCnt;
        }

        return GetTakeSituationResponse.of(applyRoundList.get(0).getRound(), applyCnt, takeCnt, noShowCnt);
    }

    public List<ApplyRound> findApplyRoundsByTakeDateBetween(LocalDate startDate, LocalDate endDate) {
        return applyRoundRepository.findAllByTakeDateBetweenAndState(startDate, endDate, ApplyRoundState.ACTIVE);
    }
}
