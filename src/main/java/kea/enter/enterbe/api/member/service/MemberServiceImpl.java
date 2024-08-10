package kea.enter.enterbe.api.member.service;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import kea.enter.enterbe.api.member.controller.dto.response.GetMemberPostReportTypeResponse;
import kea.enter.enterbe.api.member.controller.dto.response.GetMemberScoreResponse;
import kea.enter.enterbe.api.member.controller.dto.response.GetRoutingResponse;
import kea.enter.enterbe.domain.apply.entity.Apply;
import kea.enter.enterbe.domain.apply.entity.ApplyRound;
import kea.enter.enterbe.domain.apply.entity.ApplyState;
import kea.enter.enterbe.domain.apply.repository.ApplyRepository;
import kea.enter.enterbe.domain.apply.repository.ApplyRoundRepository;
import kea.enter.enterbe.domain.lottery.entity.Waiting;
import kea.enter.enterbe.domain.lottery.entity.WaitingState;
import kea.enter.enterbe.domain.lottery.entity.Winning;
import kea.enter.enterbe.domain.lottery.entity.WinningState;
import kea.enter.enterbe.domain.lottery.repository.WaitingRepository;
import kea.enter.enterbe.domain.lottery.repository.WinningRepository;
import kea.enter.enterbe.api.member.service.vo.MemberReportState;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.entity.MemberState;
import kea.enter.enterbe.domain.member.repository.MemberRepository;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import kea.enter.enterbe.global.common.period.ApplyMenuPage;
import kea.enter.enterbe.global.common.period.PeriodForApplyMenu;
import kea.enter.enterbe.global.common.period.UserStateForApplyMenu;
import kea.enter.enterbe.domain.take.entity.VehicleReport;
import kea.enter.enterbe.domain.take.entity.VehicleReportState;
import kea.enter.enterbe.domain.take.entity.VehicleReportType;
import kea.enter.enterbe.domain.take.repository.VehicleReportRepository;
import kea.enter.enterbe.domain.vehicle.repository.VehicleRepository;
import kea.enter.enterbe.global.common.api.CustomResponseCode;
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
    private final ApplyRepository applyRepository;
    private final ApplyRoundRepository applyRoundRepository;
    private final WinningRepository winningRepository;
    private final WaitingRepository waitingRepository;
    private final VehicleReportRepository vehicleReportRepository;

    private final Clock clock;

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

    @Override
    public GetRoutingResponse getRoutingInformation(Long memberId) {
        LocalDateTime now = LocalDateTime.now(clock);
        PeriodForApplyMenu period = PeriodForApplyMenu.getCurrentPeriod(now);

        // 이번주 회차 범위
        LocalDateTime startOfRound = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).withHour(9).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfRound = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        // 기간에 따라서 사용자의 상태확인 (예를 들어, 신청 기간에는 신청자인지 아닌지 확인)
        UserStateForApplyMenu state = switch (period){
            case APPLICATION_CREATE, ONLY_APPLICATION_VIEW -> getIsApplicant(memberId, startOfRound, endOfRound);
            case LOTTERY_RESULT -> getIsWinner(memberId, startOfRound, endOfRound);
            case NOTING_TODO -> UserStateForApplyMenu.EMPLOYEE;
        };

        // 사용자의 상태와 현재 기간을 통해서 라우팅할 페이지 번호 정함 (페이지 번호는 피그마 참고)
        ApplyMenuPage page = ApplyMenuPage.getCurrentPage(state, period);
        return GetRoutingResponse.of(page.getRoutingPageNum(), state.name());
    }

    // 신청 기간에 신청 여부에 따른 사용자의 상태(신청자, 사원)를 반환하는 매서드
    private UserStateForApplyMenu getIsApplicant(Long memberId, LocalDateTime startOfRound, LocalDateTime endOfRound) {
        Optional<ApplyRound> isApply = applyRoundRepository.findApplyRoundsForMemberInAndStateCurrentWeek(memberId, startOfRound, endOfRound, ApplyState.ACTIVE);

        if(isApply.isPresent()){
            return UserStateForApplyMenu.APPLICANT;
        }
        else {
            return UserStateForApplyMenu.EMPLOYEE;
        }

    }

    // 당첨 발표일부터 회차 마지막까지 당첨여부에 따른 사용자의 상태(당첨자, 대기자, 미당첨자)를 반환하는 메서드
    private UserStateForApplyMenu getIsWinner(Long memberId, LocalDateTime startOfRound, LocalDateTime endOfRound) {
        // 이번주에 작성한 신청서 가져오기
        Apply apply = findApplicantInCurrentWeek(memberId, startOfRound, endOfRound);

        Optional<Winning> isWinner = winningRepository.findByApplyAndState(apply, WinningState.ACTIVE);
        Optional<Waiting> isCandidate = waitingRepository.findByApplyAndState(apply, WaitingState.ACTIVE);
        if(isWinner.isPresent()){
            return UserStateForApplyMenu.WINNER;
        }
        // 당첨자가 아니라면
        else {
            if(isCandidate.isPresent()){
                return UserStateForApplyMenu.CANDIDATE;
            }
            else {
                return UserStateForApplyMenu.NON_WINNER;
            }
        }
    }

    private Apply findApplicantInCurrentWeek(Long memberId, LocalDateTime startOfRound, LocalDateTime endOfRound) {
        return applyRepository.findByMemberIdAndStateCurrentWeek(memberId, ApplyState.ACTIVE, startOfRound, endOfRound)
            .orElseThrow(() -> new CustomException(ResponseCode.APPLY_NOT_FOUND));
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
