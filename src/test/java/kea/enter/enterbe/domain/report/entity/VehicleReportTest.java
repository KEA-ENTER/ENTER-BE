package kea.enter.enterbe.domain.report.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import kea.enter.enterbe.IntegrationTestSupport;
import kea.enter.enterbe.domain.apply.entity.Apply;
import kea.enter.enterbe.domain.apply.entity.ApplyState;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.entity.MemberRole;
import kea.enter.enterbe.domain.member.entity.MemberState;
import kea.enter.enterbe.domain.round.entity.ApplyRound;
import kea.enter.enterbe.domain.round.entity.ApplyRoundState;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
import kea.enter.enterbe.domain.vehicle.entity.VehicleFuel;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import kea.enter.enterbe.domain.winning.entity.Winning;
import kea.enter.enterbe.domain.winning.entity.WinningState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class VehicleReportTest extends IntegrationTestSupport {

    @DisplayName(value = "인수보고서를 생성한다.")
    @Test
    public void takeCreate() throws Exception {
        //given
        LocalDate takeDate = LocalDate.of(1999, 8, 20);
        LocalDate returnDate = LocalDate.of(1999, 8, 30);
        Vehicle vehicle = createVehicle();
        Member member = createMember();
        ApplyRound applyRound = createApplyRound(vehicle, takeDate, returnDate);
        Apply apply = createApply(member, applyRound, vehicle);
        Winning winning = createWinning(vehicle, apply);
        //when
        VehicleReport vehicleReport = VehicleReport.takeCreate(winning, "image",
            "image", "image", "image", "image");
        //then
        assertThat(vehicleReport)
            .extracting("type", "state")
            .contains(VehicleReportType.TAKE, VehicleReportState.ACTIVE);
    }

    private Winning createWinning(Vehicle vehicle, Apply apply) {
        return Winning.of(vehicle, apply, WinningState.ACTIVE);
    }

    private Apply createApply(Member member, ApplyRound applyRound, Vehicle vehicle) {
        return Apply.of(member, applyRound, vehicle, "departures", "arrivals", ApplyState.ACTIVE);
    }

    private ApplyRound createApplyRound(Vehicle vehicle, LocalDate takeDate, LocalDate returnDate) {
        return ApplyRound.of(vehicle, 1, takeDate, returnDate, ApplyRoundState.ACTIVE);
    }

    private Vehicle createVehicle() {
        return Vehicle.of("vehicleNo", "company", "model", 4, VehicleFuel.DIESEL, "img",
            VehicleState.AVAILABLE);
    }

    private Member createMember() {
        return Member.of("employeeNo", "name", "email", "password", LocalDate.of(1999,1,1), "licenseId",
            "licensePassword", true, true, 1, MemberRole.USER, MemberState.ACTIVE);
    }

}