package kea.enter.enterbe.api.member.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import kea.enter.enterbe.api.member.controller.dto.response.GetMemberPostReportTypeResponse;
import kea.enter.enterbe.api.member.controller.dto.response.GetMemberScoreResponse;
import kea.enter.enterbe.api.member.service.vo.MemberReportState;
import kea.enter.enterbe.domain.lottery.entity.Winning;
import kea.enter.enterbe.domain.lottery.entity.WinningState;
import kea.enter.enterbe.domain.lottery.repository.WinningRepository;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.entity.MemberState;
import kea.enter.enterbe.domain.member.repository.MemberRepository;
import kea.enter.enterbe.domain.take.entity.VehicleReport;
import kea.enter.enterbe.domain.take.entity.VehicleReportState;
import kea.enter.enterbe.domain.take.entity.VehicleReportType;
import kea.enter.enterbe.domain.take.repository.VehicleReportRepository;
import kea.enter.enterbe.domain.vehicle.repository.VehicleRepository;
import kea.enter.enterbe.global.common.api.CustomResponseCode;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final WinningRepository winningRepository;
    private final VehicleReportRepository vehicleReportRepository;
    @Override
    public GetMemberScoreResponse getMemberScorePercent(Long memberId) {
        Long totalCount = memberRepository.countTotalMembers(MemberState.ACTIVE);
        Long higherCount = memberRepository.countMembersWithHigherScore(
            memberId, MemberState.ACTIVE);
        return GetMemberScoreResponse.of((double) higherCount / totalCount * 100);
    }

    @Override
    public GetMemberPostReportTypeResponse getMemberPostReportType(Long memberId) {
        GetMemberPostReportTypeResponse response;
        LocalDate today = LocalDate.now();
        Optional<Winning> winning = getWinningByMemberIdThisWeek(memberId,today);
        if(winning.isEmpty()) {
            return GetMemberPostReportTypeResponse.of(MemberReportState.NONE.name());
        }
        List<VehicleReport> vehicleReportList = vehicleReportRepository.findByWinningIdAndState(
            winning.get().getId(), VehicleReportState.ACTIVE);
        int takeCount = 0;
        int returnCount = 0;
        for(VehicleReport vehicleReport : vehicleReportList) {
            if(vehicleReport.getType().equals(VehicleReportType.TAKE))
                takeCount++;
            if(vehicleReport.getType().equals(VehicleReportType.RETURN))
                returnCount++;
        }
        if(takeCount==0)
            response =  GetMemberPostReportTypeResponse.of(MemberReportState.TAKE.name());
        else if(returnCount==0)
            response= GetMemberPostReportTypeResponse.of(MemberReportState.RETURN.name());
        else
            response= GetMemberPostReportTypeResponse.of(MemberReportState.NONE.name());
        return response;
    }

    private Member findByMemberId(Long memberId) {
        return memberRepository.findByIdAndState(memberId, MemberState.ACTIVE)
            .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_MEMBER));
    }

    private Optional<Winning> getWinningByMemberIdThisWeek(Long memberId, LocalDate date) {
        return winningRepository.findByApplyMemberIdAndApplyApplyRoundTakeDateBetweenAndState(
                memberId, date.with(
                    DayOfWeek.MONDAY), date.with(DayOfWeek.SUNDAY), WinningState.ACTIVE);
    }
}
