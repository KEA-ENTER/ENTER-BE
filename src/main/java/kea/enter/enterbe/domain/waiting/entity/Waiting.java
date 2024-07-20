package kea.enter.enterbe.domain.waiting.entity;

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
@Table(name = "waiting")
public class Waiting extends BaseEntity {

    @Column(name = "vehicle_id")
    private Integer vehicleId;

    @Column(name = "apply_id")
    private Integer applyId;

    @Column(name = "order")
    private Integer order;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private WaitingState state;

    @Builder
    public Waiting(int vehicleId, int applyId, int order) {
        this.vehicleId = vehicleId;
        this.applyId = applyId;
        this.order = order;
    }

    public static Waiting of(int vehicleId, int applyId, int order) {
        return Waiting.builder().vehicleId(vehicleId).applyId(applyId).order(order).build();
    }
}
