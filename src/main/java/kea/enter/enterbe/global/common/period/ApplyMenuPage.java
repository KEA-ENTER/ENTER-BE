package kea.enter.enterbe.global.common.period;

import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApplyMenuPage {
    WRITE(1){
        @Override
        public boolean whichPage(UserStateForApplyMenu state, PeriodForApplyMenu period) {
            return ((period == PeriodForApplyMenu.APPLICATION_CREATE) && (state == UserStateForApplyMenu.EMPLOYEE));
        }
    },
    VIEW(2){
        @Override
        public boolean whichPage(UserStateForApplyMenu state, PeriodForApplyMenu period) {
            return state == UserStateForApplyMenu.APPLICANT
                && (period == PeriodForApplyMenu.APPLICATION_CREATE || period == PeriodForApplyMenu.ONLY_APPLICATION_VIEW);
        }
    },
    NOTING_TODO(3){
        @Override
        public boolean whichPage(UserStateForApplyMenu state, PeriodForApplyMenu period) {
            return period == PeriodForApplyMenu.NOTING_TODO
                || (state == UserStateForApplyMenu.EMPLOYEE
                && (period == PeriodForApplyMenu.LOTTERY_RESULT || period == PeriodForApplyMenu.ONLY_APPLICATION_VIEW));
        }
    },
    RESULT(4){
        @Override
        public boolean whichPage(UserStateForApplyMenu state, PeriodForApplyMenu period) {
            return (state == UserStateForApplyMenu.WINNER || state == UserStateForApplyMenu.CANDIDATE || state == UserStateForApplyMenu.NON_WINNER)
                && (period == PeriodForApplyMenu.LOTTERY_RESULT);
        }
    };

    private final Integer routingPageNum;

    public abstract boolean whichPage(UserStateForApplyMenu state, PeriodForApplyMenu period);

    public static ApplyMenuPage getCurrentPage(UserStateForApplyMenu state, PeriodForApplyMenu period) {
        for(ApplyMenuPage page : ApplyMenuPage.values()) {
            if(page.whichPage(state, period)) {
                return page;
            }
        }
        throw new CustomException(ResponseCode.ROUTING_NOT_FOUND);
    }
}
