package kea.enter.enterbe.domain.apply.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
import kea.enter.enterbe.global.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "apply_round")
@ToString
public class ApplyRound extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(name = "round", nullable = false)
    private Integer round;

    @Column(name = "take_date")
    private LocalDate takeDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private ApplyRoundState state;

    @OneToMany(mappedBy = "applyRound", fetch = FetchType.LAZY)
    private List<Apply> applyList = new ArrayList<>();

    @Builder
    public ApplyRound(Vehicle vehicle, Integer round, LocalDate takeDate, LocalDate returnDate,
        ApplyRoundState state) {
        this.vehicle = vehicle;
        this.takeDate = takeDate;
        this.returnDate = returnDate;
        this.round = round;
        this.state = state;
    }

    public static ApplyRound of(Vehicle vehicle, Integer round, LocalDate takeDate,
        LocalDate returnDate, ApplyRoundState state) {
        return ApplyRound.builder()
            .vehicle(vehicle)
            .round(round)
            .takeDate(takeDate)
            .returnDate(returnDate)
            .state(state)
            .build();
    }
}
