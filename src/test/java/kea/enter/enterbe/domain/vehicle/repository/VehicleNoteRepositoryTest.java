package kea.enter.enterbe.domain.vehicle.repository;

import kea.enter.enterbe.IntegrationTestSupport;
import kea.enter.enterbe.domain.apply.entity.Apply;
import kea.enter.enterbe.domain.apply.entity.ApplyPurpose;
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
import kea.enter.enterbe.domain.vehicle.entity.VehicleNote;
import kea.enter.enterbe.domain.vehicle.entity.VehicleNoteState;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class VehicleNoteRepositoryTest extends IntegrationTestSupport {
    @DisplayName("해당 차량의 차량 특이사항을 조회한다.")
    @Test
    void findByVehicleIdAndState() {
        // given
        Vehicle vehicle = vehicleRepository.save(createVehicle());
        ApplyRound applyRound = applyRoundRepository.save(createApplyRound(vehicle, LocalDate.of(2024, 7, 29)));

        Member member = memberRepository.save(createMember());
        Apply apply = applyRepository.save(createApply(member, applyRound));
        Winning winning = winningRepository.save(createWinning(apply, WinningState.ACTIVE));

        VehicleReport vehicleReport = vehicleReportRepository.save(createTakeVehicleReport(winning));
        vehicleNoteRepository.save(createVehicleNote(vehicle, vehicleReport));

        // when
        Optional<VehicleNote> response = vehicleNoteRepository.findByVehicleIdAndState(vehicle.getId(), VehicleNoteState.ACTIVE);

        // then
        assertThat(response).isPresent();
    }

    private Member createMember() {
        return Member.of("name", "email", "password", LocalDate.of(1999,11,28),
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

    private Apply createApply(Member member, ApplyRound applyRound) {
        return Apply.of(member, applyRound, ApplyPurpose.EVENT, ApplyState.ACTIVE);
    }

    private Winning createWinning(Apply apply, WinningState state) {
        return Winning.of(apply, state);
    }

    private VehicleReport createTakeVehicleReport(Winning winning) {
        return VehicleReport.of(winning, winning.getApply().getApplyRound().getVehicle(),"frontImg","leftImg", "rightImg", "backImg", "dashboardImg",
            null, VehicleReportType.TAKE, VehicleReportState.ACTIVE);
    }

    private VehicleNote createVehicleNote(Vehicle vehicle, VehicleReport report) {
        return VehicleNote.of(vehicle, report, "차가 구려요", VehicleNoteState.ACTIVE);
    }
}