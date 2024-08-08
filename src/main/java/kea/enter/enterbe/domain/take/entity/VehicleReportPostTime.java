package kea.enter.enterbe.domain.take.entity;

import java.time.LocalTime;
import lombok.Getter;

@Getter
public enum VehicleReportPostTime {
    TAKE_TIME(LocalTime.of(17, 0)),
    RETURN_TIME(LocalTime.of(9, 0));
    LocalTime time;

    VehicleReportPostTime(LocalTime time) {
        this.time = time;
    }
}
