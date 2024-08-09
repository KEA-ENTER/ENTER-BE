package kea.enter.enterbe.global.common.period;

import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

public enum PeriodForApplyMenu {
    // 1. 월요일 09:00:00~화요일 23:59:59 -> 신청서 작성 가능 기간(신청서 취소 수정 포함)
    APPLICATION_CREATE {
        @Override
        public boolean isActive(LocalDateTime now) {
            DayOfWeek startDay = DayOfWeek.MONDAY;
            LocalTime startTime = LocalTime.of(9, 0, 0);

            DayOfWeek endDay = DayOfWeek.TUESDAY;
            LocalTime endTime = LocalTime.of(23, 59, 59);

            DayOfWeek currentDay = now.getDayOfWeek();
            LocalTime currentTime = now.toLocalTime();
            return (currentDay.equals(startDay) && (currentTime.isAfter(startTime)
                || currentTime.equals(startTime)))
                || (currentDay.equals(endDay) && (currentTime.isBefore(endTime)));
        }
    },
    // 2. 수요일 00:00:00~09:59:59 -> 조회 가능, 수정 취소 불가능 기간
    ONLY_APPLICATION_VIEW {
        @Override
        public boolean isActive(LocalDateTime now) {
            DayOfWeek startDay = DayOfWeek.TUESDAY;
            LocalTime startTime = LocalTime.of(23, 59, 59);

            DayOfWeek endDay = DayOfWeek.WEDNESDAY;
            LocalTime endTime = LocalTime.of(10, 0, 0);

            DayOfWeek currentDay = now.getDayOfWeek();
            LocalTime currentTime = now.toLocalTime();
            return (currentDay.equals(startDay) && (currentTime.isAfter(startTime)
                || currentTime.equals(startTime)))
                || (currentDay.equals(endDay) && (currentTime.isBefore(endTime)));
        }
    },
    // 3. 수요일 10:00:00~일요일 23:59:59 -> 당첨 결과 확인 페이지
    LOTTERY_RESULT {
        @Override
        public boolean isActive(LocalDateTime now) {
            DayOfWeek startDay = DayOfWeek.WEDNESDAY;
            LocalTime startTime = LocalTime.of(10, 0, 0);

            DayOfWeek endDay = DayOfWeek.SUNDAY;
            LocalTime endTime = LocalTime.of(23, 59, 59);

            DayOfWeek currentDay = now.getDayOfWeek();
            LocalTime currentTime = now.toLocalTime();
            return (currentDay.equals(startDay) && (currentTime.isAfter(startTime)
                || currentTime.equals(startTime)))
                || currentDay.equals(DayOfWeek.THURSDAY)
                || currentDay.equals(DayOfWeek.FRIDAY)
                || currentDay.equals(DayOfWeek.SATURDAY)
                || (currentDay.equals(endDay) && (currentTime.isBefore(endTime)));
        }
    },
    // 4. 월요일 00:00:00~08:59:59 -> 아직 신청기간 아니에요 페이지
    NOTING_TODO {
        @Override
        public boolean isActive(LocalDateTime now) {
            DayOfWeek startDay = DayOfWeek.SUNDAY;
            LocalTime startTime = LocalTime.of(23, 59, 59);

            DayOfWeek endDay = DayOfWeek.MONDAY;
            LocalTime endTime = LocalTime.of(9, 0, 0);

            DayOfWeek currentDay = now.getDayOfWeek();
            LocalTime currentTime = now.toLocalTime();
            return (currentDay.equals(startDay) && (currentTime.isAfter(startTime)
                || currentTime.equals(startTime)))
                || (currentDay.equals(endDay) && (currentTime.isBefore(endTime)));
        }
    };

    public abstract boolean isActive(LocalDateTime now);

    public static PeriodForApplyMenu getCurrentPeriod(LocalDateTime now) {
        for (PeriodForApplyMenu period : PeriodForApplyMenu.values()) {
            if (period.isActive(now)) {
                return period;
            }
        }
        throw new CustomException(ResponseCode.BAD_REQUEST);
    }
}