package kea.enter.enterbe.api.member.service;

import kea.enter.enterbe.api.member.controller.dto.response.GetMemberScoreResponse;
import kea.enter.enterbe.api.member.controller.dto.response.GetRoutingResponse;
import kea.enter.enterbe.domain.member.entity.MemberState;
import kea.enter.enterbe.domain.member.repository.MemberRepository;
import kea.enter.enterbe.global.common.period.PeriodForApplyMenu;
import kea.enter.enterbe.global.common.period.ApplyMenuPage;
import kea.enter.enterbe.global.common.period.UserStateForApplyMenu;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Clock;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final Clock clock;

    @Override
    public GetMemberScoreResponse getMemberScorePercent(Long memberId) {
        Long totalCount = memberRepository.countTotalMembers(MemberState.ACTIVE);
        Long higherCount = memberRepository.countMembersWithHigherScore(
            memberId, MemberState.ACTIVE);
        return GetMemberScoreResponse.of((double) higherCount / totalCount * 100);
    }

    @Override
    public GetRoutingResponse getRoutingInformation(Long memberId) {
        LocalDateTime now = LocalDateTime.now(clock);
        PeriodForApplyMenu period = PeriodForApplyMenu.getCurrentPeriod(now);

        // 기간에 따라서 사용자의 상태확인 (예를 들어, 신청 기간에는 신청자인지 아닌지 확인)
        UserStateForApplyMenu state = switch (period){
            case APPLICATION_CREATE, ONLY_APPLICATION_VIEW -> getIsApplicant(memberId);
            case LOTTERY_RESULT -> getIsWinner(memberId);
            case NOTING_TODO -> UserStateForApplyMenu.EMPLOYEE;
        };

        // 사용자의 상태와 현재 기간을 통해서 라우팅할 페이지 번호 정함 (페이지 번호는 피그마 참고)
        ApplyMenuPage page = ApplyMenuPage.getCurrentPage(state, period);
        return GetRoutingResponse.of(page.getRoutingPageNum(), state.name());
    }

    // 신청 기간에 신청 여부에 따른 사용자의 상태(신청자, 사원)를 반환하는 매서드
    private UserStateForApplyMenu getIsApplicant(Long memberId) {
        boolean flag = true;
        if(flag){
            return UserStateForApplyMenu.APPLICANT;
        }else {
            return UserStateForApplyMenu.EMPLOYEE;
        }
    }

    // 당첨 발표일부터 회차 마지막까지 당첨여부에 따른 사용자의 상태(당첨자, 대기자, 미당첨자)를 반환하는 메서드
    private UserStateForApplyMenu getIsWinner(Long memberId) {
        boolean flag = true;
        if(flag){
            return UserStateForApplyMenu.WINNER;
        }else if(!flag) {
            return UserStateForApplyMenu.CANDIDATE;
        }else{
            return UserStateForApplyMenu.NON_WINNER;
        }
    }

}
