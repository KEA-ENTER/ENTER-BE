package kea.enter.enterbe.domain.waiting.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kea.enter.enterbe.domain.apply.entity.Apply;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
import kea.enter.enterbe.global.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "waiting")
public class Waiting extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apply_id", nullable = false)
    private Apply apply;

    @Column(name = "waiting_no")
    private Integer waiting_no;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private WaitingState state;

    @Builder
    public Waiting(Vehicle vehicle, Apply apply, int waiting_no) {
        this.vehicle = vehicle;
        this.apply = apply;
        this.waiting_no = waiting_no;
    }

    public static Waiting of(Vehicle vehicle, Apply apply, int waiting_no) {
        return Waiting.builder().vehicle(vehicle).apply(apply).waiting_no(waiting_no).build();
    }
}
