package kea.enter.enterbe.domain.winning.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "winning")
public class Winning extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    // 추후 수정예정
    @Column(name = "apply_id", nullable = false)
    private Integer apply;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private WinningState state;

    // apply 수정에 따라 추후 수정예정
    @Builder
    public Winning(Vehicle vehicle, Integer apply, WinningState state) {
        this.vehicle = vehicle;
        this.apply = apply;
        this.state = state;
    }

    // apply 수정에 따라 추후 수정예정
    public static Winning of(Vehicle vehicle, Integer apply, WinningState state) {
        return Winning.builder().vehicle(vehicle).apply(apply).state(state).build();
    }
}
