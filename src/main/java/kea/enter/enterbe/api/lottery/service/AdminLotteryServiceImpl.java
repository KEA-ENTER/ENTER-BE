package kea.enter.enterbe.api.lottery.service;

import kea.enter.enterbe.api.lottery.controller.dto.response.GetLotteryListResponse;
import kea.enter.enterbe.api.lottery.controller.dto.response.LotteryInfo;
import kea.enter.enterbe.api.lottery.service.dto.GetLotteryListServiceDto;
import kea.enter.enterbe.domain.apply.entity.Apply;
import kea.enter.enterbe.domain.apply.entity.ApplyRound;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminLotteryServiceImpl implements AdminLotteryService {
    private final ApplyRoundRepository applyRoundRepository;
    private final ApplyRepository applyRepository;
    private final WinningRepository winningRepository;
    private final VehicleReportRepository vehicleReportRepository;

    /* 추첨 관리 목록 조회 API */
    @Override
    public GetLotteryListResponse getLotteryList(GetLotteryListServiceDto dto) {
        Page<ApplyRound> applyRoundList = applyRoundRepository.findAllApplyRoundByCondition(dto.getKeyword(), dto.getSearchType(), dto.getPageable());

        List<LotteryInfo> lotteryInfoList = new ArrayList<>();
        for (ApplyRound applyRound : applyRoundList.getContent()) {
            // 신청회차 정보 조회
            List<Apply> applyList = applyRepository.findAllByApplyRoundIdAndState(applyRound.getId(), ApplyState.ACTIVE);
            int applyCnt = applyList.size();

            List<Winning> winningList = winningRepository.findAllByApplyApplyRoundIdAndState(applyRound.getId(), WinningState.ACTIVE);
            int winningCnt = winningList.size();

            List<VehicleReport> vehicleReportList = vehicleReportRepository.findAllByWinningApplyApplyRoundIdAndTypeAndState(applyRound.getId(), VehicleReportType.TAKE, VehicleReportState.ACTIVE);
            int noShowCnt = winningCnt - vehicleReportList.size();

            String competition = winningCnt + ":" + applyCnt;

            // 신청회차 정보 입력
            lotteryInfoList.add(LotteryInfo.of(
                applyRound.getId(),
                applyRound.getApplyRound(),
                applyRound.getTakeDate().toString(), applyRound.getReturnDate().toString(),
                applyRound.getVehicle().getModel(), applyRound.getVehicle().getVehicleNo(),
                applyCnt, winningCnt, noShowCnt, competition));
        }

        return GetLotteryListResponse.of(
            lotteryInfoList,
            applyRoundList.getNumber(),
            applyRoundList.getSize(),
            applyRoundList.getTotalElements(),
            applyRoundList.getTotalPages(),
            applyRoundList.hasNext());
    }
}
