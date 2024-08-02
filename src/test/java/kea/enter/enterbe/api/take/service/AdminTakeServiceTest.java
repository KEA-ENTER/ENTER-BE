package kea.enter.enterbe.api.take.service;

import kea.enter.enterbe.IntegrationTestSupport;
import kea.enter.enterbe.api.take.controller.response.GetTakeSituationResponse;
import kea.enter.enterbe.api.take.service.dto.GetTakeSituationServiceDto;
import kea.enter.enterbe.domain.apply.entity.Apply;
import kea.enter.enterbe.domain.apply.entity.ApplyRound;
import kea.enter.enterbe.domain.apply.entity.ApplyRoundState;
import kea.enter.enterbe.domain.apply.entity.ApplyState;
import kea.enter.enterbe.domain.lottery.entity.Winning;
import kea.enter.enterbe.domain.lottery.entity.WinningState;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.entity.MemberRole;
import kea.enter.enterbe.domain.member.entity.MemberState;
import kea.enter.enterbe.domain.take.entity.VehicleReport;
import kea.enter.enterbe.domain.take.entity.VehicleReportState;
import kea.enter.enterbe.domain.take.entity.VehicleReportType;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
import kea.enter.enterbe.domain.vehicle.entity.VehicleFuel;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.DayOfWeek;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class AdminTakeServiceTest extends IntegrationTestSupport {
    @DisplayName("저번주의 인수 현황을 조회한다.")
    @Test
    void getTakeSituation() {
        // given
        LocalDate now  = LocalDate.now();

        LocalDate date = now.minusDays(7);
        LocalDate lastMonday = date.with(DayOfWeek.MONDAY);  // 저번주 월요일
        LocalDate lastSunday = date.with(DayOfWeek.SUNDAY);  // 저번주 일요일

        Vehicle vehicle1 = vehicleRepository.save(createVehicle());
        Vehicle vehicle2 = vehicleRepository.save(createVehicle());

        // 경계값으로 테스트
        ApplyRound applyRound1 = applyRoundRepository.save(createApplyRound(vehicle1, lastMonday));
        ApplyRound applyRound2 = applyRoundRepository.save(createApplyRound(vehicle2, lastSunday));

        Member member1 = memberRepository.save(createMember());
        Member member2 = memberRepository.save(createMember());

        Apply apply1 = applyRepository.save(createApply(member1, applyRound1, vehicle1));
        Apply apply2 = applyRepository.save(createApply(member2, applyRound2, vehicle2));

        Winning winning = winningRepository.save(createWinning(vehicle1, apply1, WinningState.ACTIVE));
        winningRepository.save(createWinning(vehicle2, apply2, WinningState.INACTIVE));

        vehicleReportRepository.save(createVehicleReport(winning));

        GetTakeSituationServiceDto dto = GetTakeSituationServiceDto.of(now);

        // when
        GetTakeSituationResponse response = adminTakeService.getTakeSituation(dto);

        // then
        assertThat(response)
            .extracting("applyRound", "applyCnt", "takeCnt", "noShowCnt")
            .contains(1, 2, 1, 1);
    }

    private Member createMember() {
        return Member.of("employeeNo", "name", "email", "password",
            "licenseId", "licensePassword", true, true,
            1, MemberRole.USER, MemberState.ACTIVE);
    }

    private Vehicle createVehicle() {
        return Vehicle.of("vehicleNo", "company", "model", 4,
            VehicleFuel.DIESEL, "img", VehicleState.AVAILABLE);
    }

    private ApplyRound createApplyRound(Vehicle vehicle, LocalDate takeDate) {
        return ApplyRound.of(vehicle, 1, takeDate, takeDate.plusDays(1), ApplyRoundState.ACTIVE);
    }

    private Apply createApply(Member member, ApplyRound applyRound, Vehicle vehicle) {
        return Apply.of(member, applyRound, vehicle, "departures", "arrivals", ApplyState.ACTIVE);
    }

    private Winning createWinning(Vehicle vehicle, Apply apply, WinningState state) {
        return Winning.of(vehicle, apply, state);
    }

    private VehicleReport createVehicleReport(Winning winning) {
        return VehicleReport.of(winning, "frontImg", "leftImg", "rightImg", "backImg", "dashboardImg",
            "parkingLoc", VehicleReportType.TAKE, VehicleReportState.ACTIVE);
    }
}