package kea.enter.enterbe.domain.report.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kea.enter.enterbe.domain.winning.entity.Winning;
import kea.enter.enterbe.global.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "vehicle_report")
public class VehicleReport extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winning_id", nullable = false)
    private Winning winning;

    @Column(name = "front_img")
    private String frontImg;

    @Column(name = "left_img")
    private String leftImg;

    @Column(name = "right_img")
    private String rightImg;

    @Column(name = "back_img")
    private String backImg;

    @Column(name = "dashboard_img")
    private String dashboardImg;

    @Column(name = "parking_loc")
    private String parkingLoc;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private VehicleReportType type;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private VehicleReportState state;

    @Builder
    public VehicleReport(Winning winning, String frontImg, String leftImg, String rightImg,
        String backImg, String dashboardImg, String parkingLoc, VehicleReportType type,
        VehicleReportState state) {
        this.winning = winning;
        this.frontImg = frontImg;
        this.leftImg = leftImg;
        this.rightImg = rightImg;
        this.backImg = backImg;
        this.dashboardImg = dashboardImg;
        this.parkingLoc = parkingLoc;
        this.type = type;
        this.state = state;
    }

    public static VehicleReport of(Winning winning, String frontImg, String leftImg, String rightImg,
        String backImg, String dashboardImg, String parkingLoc, VehicleReportType type,
        VehicleReportState state) {
        return VehicleReport.builder()
            .winning(winning)
            .frontImg(frontImg)
            .leftImg(leftImg)
            .rightImg(rightImg)
            .backImg(backImg)
            .dashboardImg(dashboardImg)
            .parkingLoc(parkingLoc)
            .type(type)
            .state(state)
            .build();
    }
}
