package kea.enter.enterbe.domain.round.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
import kea.enter.enterbe.global.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="apply_round")
public class ApplyRound extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(name="applu_round", nullable = false)
    private Integer applyRound;

    @Enumerated(EnumType.STRING)
    @Column(name="state", nullable = false)
    private ApplyRoundState state;

    @Builder
    public ApplyRound(Vehicle vehicle, Integer applyRound, ApplyRoundState state){
        this.vehicle = vehicle;
        this.applyRound = applyRound;
        this.state = state;
    }

    public static ApplyRound of(Vehicle vehicle, Integer applyRound, ApplyRoundState state){
        return ApplyRound.builder()
            .vehicle(vehicle)
            .applyRound(applyRound)
            .state(state)
            .build();
    }
}