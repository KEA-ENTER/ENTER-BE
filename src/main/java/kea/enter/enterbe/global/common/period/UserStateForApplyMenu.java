package kea.enter.enterbe.global.common.period;

import lombok.Getter;

// 용어사전 참조, 차량 신청 메뉴에 필요한 사원, 신청자, 당첨자, 대기자, 미당첨자
@Getter
public enum UserStateForApplyMenu {
    EMPLOYEE,
    APPLICANT,
    WINNER,
    CANDIDATE,
    NON_WINNER
}
