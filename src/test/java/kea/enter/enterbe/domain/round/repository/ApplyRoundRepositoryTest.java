package kea.enter.enterbe.domain.round.repository;

import kea.enter.enterbe.IntegrationTestSupport;
import kea.enter.enterbe.domain.apply.entity.ApplyRound;
import kea.enter.enterbe.domain.apply.entity.ApplyRoundState;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
import kea.enter.enterbe.domain.vehicle.entity.VehicleFuel;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class ApplyRoundRepositoryTest extends IntegrationTestSupport {
    @DisplayName(value = "해당 주에 진행하는 신청 회차 목록을 조회한다.")
    @Test
    public void findAllByTakeDateBetweenAndState() {
        // given
        Vehicle vehicle1 = vehicleRepository.save(createVehicle());
        Vehicle vehicle2 = vehicleRepository.save(createVehicle());

        ApplyRound applyRound1 = createApplyRound(vehicle1, LocalDate.of(2024, 7, 29));
        ApplyRound applyRound2 = createApplyRound(vehicle2, LocalDate.of(2024, 8, 4));
        applyRoundRepository.saveAll(List.of(applyRound1, applyRound2));

        // when
        List<ApplyRound> applyRoundList = applyRoundRepository.findAllByTakeDateBetweenAndState(LocalDate.of(2024, 7, 29), LocalDate.of(2024, 8, 4), ApplyRoundState.ACTIVE);

        // then
        assertThat(applyRoundList).hasSize(2)
            .extracting("id", "state")
            .containsExactlyInAnyOrder(
                tuple(applyRound1.getId(), ApplyRoundState.ACTIVE),
                tuple(applyRound2.getId(), ApplyRoundState.ACTIVE)
            );
    }

    @DisplayName(value = "해당 주에 진행하는 신청 회차 목록이 아니라면 조회되지 않는다.")
    @Test
    public void findAllByTakeDateBetweenAndStateWithDifferentRangeOfTakeDate() {
        // given
        LocalDate now = LocalDate.of(2024, 7, 31);
        LocalDate thisMonday = now.with(DayOfWeek.MONDAY);  // 해당주 월요일
        LocalDate thisSunday = thisMonday.plusDays(6);  // 해당주 일요일

        Vehicle vehicle1 = vehicleRepository.save(createVehicle());
        Vehicle vehicle2 = vehicleRepository.save(createVehicle());

        ApplyRound applyRound1 = createApplyRound(vehicle1, LocalDate.of(2024, 6, 1));
        ApplyRound applyRound2 = createApplyRound(vehicle2, LocalDate.of(2024, 7, 29));
        applyRoundRepository.saveAll(List.of(applyRound1, applyRound2));

        // when
        List<ApplyRound> applyRoundList = applyRoundRepository.findAllByTakeDateBetweenAndState(thisMonday, thisSunday, ApplyRoundState.ACTIVE);

        // then
        assertThat(applyRoundList).hasSize(1)
            .extracting("id", "takeDate", "state")
            .containsExactlyInAnyOrder(
                tuple(applyRound2.getId(), LocalDate.of(2024, 7, 29), ApplyRoundState.ACTIVE)
            );
    }

    private Vehicle createVehicle() {
        return Vehicle.of("vehicleNo", "company", "model", 4,
            VehicleFuel.DIESEL, "img", VehicleState.AVAILABLE);
    }

    private ApplyRound createApplyRound(Vehicle vehicle, LocalDate takeDate) {
        return ApplyRound.of(vehicle, 1, takeDate, takeDate.plusDays(1), ApplyRoundState.ACTIVE);
    }

}