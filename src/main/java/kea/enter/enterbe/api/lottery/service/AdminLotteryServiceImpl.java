package kea.enter.enterbe.api.lottery.service;

import kea.enter.enterbe.api.lottery.controller.dto.response.ApplicantInfo;
import kea.enter.enterbe.api.lottery.controller.dto.response.GetApplicantListResponse;
import kea.enter.enterbe.api.lottery.controller.dto.response.GetLotteryListResponse;
import kea.enter.enterbe.api.lottery.controller.dto.response.LotteryInfo;
import kea.enter.enterbe.api.lottery.service.dto.GetApplicantListServiceDto;
import kea.enter.enterbe.api.lottery.service.dto.GetLotteryListServiceDto;
import kea.enter.enterbe.domain.apply.entity.Apply;
import kea.enter.enterbe.domain.apply.entity.ApplyRound;
import kea.enter.enterbe.domain.apply.entity.ApplyRoundState;
import kea.enter.enterbe.domain.apply.entity.ApplyState;
import kea.enter.enterbe.domain.apply.repository.ApplyRepository;
import kea.enter.enterbe.domain.apply.repository.ApplyRoundRepository;
import kea.enter.enterbe.domain.lottery.entity.Winning;
import kea.enter.enterbe.domain.lottery.entity.WinningState;
import kea.enter.enterbe.domain.lottery.repository.WinningRepository;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.take.entity.VehicleReport;
import kea.enter.enterbe.domain.take.entity.VehicleReportState;
import kea.enter.enterbe.domain.take.entity.VehicleReportType;
import kea.enter.enterbe.domain.take.repository.VehicleReportRepository;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
                applyRound.getRound(),
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

    /* 신청 내역 목록 조회 API */
    @Override
    public GetApplicantListResponse getApplicantList(GetApplicantListServiceDto dto) {
        // 신청 회차 존재 여부 확인
        ApplyRound applyRound = findApplyRoundById(dto.getApplyRoundId());

        // 해당 신청 회차의 신청 내역 조회 (검색, 페이징)
        Page<Apply> applyList = applyRepository.findAllApplyByCondition(applyRound.getId(), dto.getKeyword(), dto.getSearchType(), dto.getPageable());

        List<ApplicantInfo> applicantInfoList = new ArrayList<>();
        for (Apply apply : applyList.getContent()) {
            // 해당 신청의 당첨 여부 조회
            Optional<Winning> winning = winningRepository.findByApplyIdAndState(apply.getId(), WinningState.ACTIVE);
            boolean isWinning = winning.isPresent();

            Member applyMember = apply.getMember();
            ApplicantInfo applicantInfo = ApplicantInfo.of(
                applyMember.getEmail(), applyMember.getName(), apply.getPurpose(), isWinning, localDateTimeToString(apply.getCreatedAt()));
            applicantInfoList.add(applicantInfo);
        }

        return GetApplicantListResponse.of(
            applyRound.getRound(),
            applyRound.getTakeDate().toString(),
            applyRound.getVehicle().getModel(),
            applyRound.getVehicle().getVehicleNo(),
            applicantInfoList,
            applyList.getNumber(),
            applyList.getSize(),
            applyList.getTotalElements(),
            applyList.getTotalPages(),
            applyList.hasNext());
    }

    // 신청 회차 존재 여부 확인
    public ApplyRound findApplyRoundById(Long applyRoundId) {
        return applyRoundRepository.findByIdAndState(applyRoundId, ApplyRoundState.ACTIVE)
            .orElseThrow(() -> new CustomException(ResponseCode.APPLY_ROUND_NOT_FOUND));
    }

    // 시간 형식 변환
    public String localDateTimeToString(LocalDateTime localDateTime) {
        Date date = java.sql.Timestamp.valueOf(localDateTime);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }
}
