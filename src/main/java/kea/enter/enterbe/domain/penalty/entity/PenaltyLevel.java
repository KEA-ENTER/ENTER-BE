package kea.enter.enterbe.domain.penalty.entity;

import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;

public enum PenaltyLevel {
    MINIMUM, LOW, MEDIUM, HIGH, BLACKLIST;

    public String getDuration(PenaltyLevel level) {
        switch (level) {
            case MINIMUM:
                return "2주";
            case LOW:
                return "4주";
            case MEDIUM:
                return "16주";
            case HIGH:
                return "64주";
            case BLACKLIST:
                return "영구정지";
            default:
                throw new CustomException(ResponseCode.PENALTY_LEVEL_NOT_FOUND);
        }
    }
}
