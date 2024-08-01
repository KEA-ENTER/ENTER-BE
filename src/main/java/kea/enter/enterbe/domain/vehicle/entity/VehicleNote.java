package kea.enter.enterbe.domain.vehicle.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kea.enter.enterbe.domain.take.entity.VehicleReport;
import kea.enter.enterbe.global.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "vehicle_note")
public class VehicleNote extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private VehicleReport vehicleReport;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private VehicleNoteState state;

    @Builder
    public VehicleNote(Vehicle vehicle, VehicleReport vehicleReport, String content,
        VehicleNoteState state) {
        this.vehicle = vehicle;
        this.vehicleReport = vehicleReport;
        this.content = content;
        this.state = state;
    }

    public static VehicleNote of(Vehicle vehicle, VehicleReport vehicleReport, String content,
        VehicleNoteState state) {
        return VehicleNote.builder()
            .vehicle(vehicle)
            .vehicleReport(vehicleReport)
            .content(content)
            .state(state)
            .build();
    }

    public static VehicleNote create(Vehicle vehicle, VehicleReport vehicleReport, String content) {
        return VehicleNote.builder()
            .vehicle(vehicle)
            .vehicleReport(vehicleReport)
            .content(content)
            .state(VehicleNoteState.ACTIVE)
            .build();
    }
}
