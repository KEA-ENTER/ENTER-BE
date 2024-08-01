package kea.enter.enterbe.domain.note.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import kea.enter.enterbe.IntegrationTestSupport;
import kea.enter.enterbe.domain.apply.entity.Apply;
import kea.enter.enterbe.domain.apply.entity.ApplyState;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.entity.MemberRole;
import kea.enter.enterbe.domain.member.entity.MemberState;
import kea.enter.enterbe.domain.take.entity.VehicleReport;
import kea.enter.enterbe.domain.take.entity.VehicleReportState;
import kea.enter.enterbe.domain.take.entity.VehicleReportType;
import kea.enter.enterbe.domain.apply.entity.ApplyRound;
import kea.enter.enterbe.domain.apply.entity.ApplyRoundState;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
import kea.enter.enterbe.domain.vehicle.entity.VehicleFuel;
import kea.enter.enterbe.domain.vehicle.entity.VehicleNote;
import kea.enter.enterbe.domain.vehicle.entity.VehicleNoteState;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import kea.enter.enterbe.domain.lottery.entity.Winning;
import kea.enter.enterbe.domain.lottery.entity.WinningState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class VehicleNoteTest extends IntegrationTestSupport {

    @DisplayName("차량 특이사항을 생성한다.")
    @Test
    void create() {
        //given
        LocalDate takeDate = LocalDate.of(1999,8,20);
        LocalDate returnDate = LocalDate.of(1999,8,30);
        Vehicle vehicle = createVehicle();
        Member member = createMember();
        ApplyRound applyRound = createApplyRound(vehicle, takeDate, returnDate);
        Apply apply = createApply(member, applyRound, vehicle);
        Winning winning = createWinning(vehicle, apply);
        VehicleReport vehicleReport = createVehicleReport(winning);
        //when
        VehicleNote vehicleNote = VehicleNote.create(vehicle, vehicleReport, "content");
        //then
        assertThat(vehicleNote)
            .extracting("state")
            .isEqualTo(VehicleNoteState.ACTIVE);
    }

    private VehicleReport createVehicleReport(Winning winning) {
        return VehicleReport.of(winning, "image", "image", "image", "image", "image",
            "location", VehicleReportType.TAKE, VehicleReportState.ACTIVE);
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

    private Member createMember() {
        return Member.of("employeeNo", "name", "email", "password", "licenseId",
            "licensePassword", true, true, 1, MemberRole.USER, MemberState.ACTIVE);
    }

    private Vehicle createVehicle() {
        return Vehicle.of("vehicleNo", "company", "model", 4, VehicleFuel.DIESEL, "img",
            VehicleState.AVAILABLE);
    }

}