package kea.enter.enterbe.domain.report.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
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

    @Column(name = "winning_id")
    private Integer winningId;

    @Column(name = "front_image")
    private String frontImage;

    @Column(name = "left_image")
    private String leftImage;

    @Column(name = "right_image")
    private String rightImage;

    @Column(name = "back_image")
    private String backImage;

    @Column(name = "dashboard_image")
    private String dashboardImage;

    @Column(name = "parking_location")
    private String parkingLocation;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private VehicleReportType type;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private VehicleReportState state;

    @Builder
    public VehicleReport(Integer winningId, String frontImage, String leftImage, String rightImage,
        String backImage, String dashboardImage, String parkingLocation, VehicleReportType type,
        VehicleReportState state) {
        this.winningId = winningId;
        this.frontImage = frontImage;
        this.leftImage = leftImage;
        this.rightImage = rightImage;
        this.backImage = backImage;
        this.dashboardImage = dashboardImage;
        this.parkingLocation = parkingLocation;
        this.type = type;
        this.state = state;
    }

    public static VehicleReport of(Integer winningId, String frontImage, String leftImage, String rightImage,
        String backImage, String dashboardImage, String parkingLocation, VehicleReportType type,
        VehicleReportState state) {
        return VehicleReport.builder()
            .winningId(winningId)
            .frontImage(frontImage)
            .leftImage(leftImage)
            .rightImage(rightImage)
            .backImage(backImage)
            .dashboardImage(dashboardImage)
            .parkingLocation(parkingLocation)
            .type(type)
            .state(state)
            .build();
    }
}
