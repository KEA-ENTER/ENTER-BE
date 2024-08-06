package kea.enter.enterbe.api.apply.service;

import kea.enter.enterbe.IntegrationTestSupport;
import kea.enter.enterbe.api.apply.controller.dto.response.GetApplyResponse;
import kea.enter.enterbe.api.apply.service.dto.GetApplyServiceDto;
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
import static org.assertj.core.groups.Tuple.tuple;

public class ApplyServiceTest extends IntegrationTestSupport {
    @DisplayName("신청 가능한 날짜를 조회한다.")
    @Test
    void getApply() {
        // given
        LocalDate today  = LocalDate.now();
        LocalDate nextTuesday = today.with(DayOfWeek.TUESDAY).plusDays(7); // 다음 주 화요일
        LocalDate nextThursday = today.with(DayOfWeek.THURSDAY).plusDays(7); // 다음 주 목요일
        LocalDate nextSaturday = today.with(DayOfWeek.SATURDAY).plusDays(7); // 다음 주 토요일
        Vehicle vehicle = vehicleRepository.save(createVehicle());

        // 같은 주에 3개의 신청이 존재 회차는 1회차로 동일, 요일이 다름
        // 다음 주 화
        applyRoundRepository.save(createApplyRound1(vehicle, nextTuesday));
        // 다음 주 목
        applyRoundRepository.save(createApplyRound1(vehicle, nextThursday));
        // 다음 주 토~일
        applyRoundRepository.save(createApplyRound2(vehicle, nextSaturday));

        GetApplyServiceDto dto = GetApplyServiceDto.of(today);

        // when
        List<GetApplyResponse> response = applyService.getApply(dto);


        // then
        assertThat(response)
            .hasSize(3)
            .extracting("takeDate", "returnDate")
            .containsExactlyInAnyOrder(
                tuple(nextTuesday,nextTuesday),
                tuple(nextThursday,nextThursday),
                tuple(nextSaturday,nextSaturday.plusDays(1))
            );

    }
    private Vehicle createVehicle() {
        return Vehicle.of("vehicleNo", "company", "model", 4,
            VehicleFuel.DIESEL, "img", VehicleState.AVAILABLE);
    }
    private ApplyRound createApplyRound1(Vehicle vehicle, LocalDate takeDate) {
        return ApplyRound.of(vehicle, 1, takeDate, takeDate, ApplyRoundState.ACTIVE);
    }
    private ApplyRound createApplyRound2(Vehicle vehicle, LocalDate takeDate) {
        System.out.println("Creating ApplyRound2 with takeDate: " + takeDate + " and returnDate: " + takeDate.plusDays(1));
        return ApplyRound.of(vehicle, 1, takeDate, takeDate.plusDays(1), ApplyRoundState.ACTIVE);
    }
}
