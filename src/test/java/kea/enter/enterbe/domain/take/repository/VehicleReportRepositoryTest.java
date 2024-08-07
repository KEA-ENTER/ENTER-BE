package kea.enter.enterbe.domain.take.repository;

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
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class VehicleReportRepositoryTest extends IntegrationTestSupport {
    @DisplayName(value = "해당 신청 회차의 당첨 목록을 조회한다.")
    @Test
    public void findAllByWinningApplyApplyRoundIdAndTypeAndState() {
        // given
        Vehicle vehicle = vehicleRepository.save(createVehicle());

        ApplyRound applyRound = applyRoundRepository.save(createApplyRound(vehicle, LocalDate.of(2024, 7, 29)));

        Member member1 = memberRepository.save(createMember());
        Member member2 = memberRepository.save(createMember());

        Apply apply1 = applyRepository.save(createApply(member1, applyRound, vehicle));
        Apply apply2 = applyRepository.save(createApply(member2, applyRound, vehicle));

        Winning winning1 = createWinning(vehicle, apply1, WinningState.ACTIVE);
        Winning winning2 = createWinning(vehicle, apply2, WinningState.ACTIVE);
        winningRepository.saveAll(List.of(winning1, winning2));

        // when
        List<Winning> winningList = winningRepository.findAllByApplyApplyRoundIdAndState(applyRound.getId(), WinningState.ACTIVE);

        // then
        assertThat(winningList).hasSize(2)
            .extracting("id", "state")
            .containsExactlyInAnyOrder(
                tuple(winning1.getId(), WinningState.ACTIVE),
                tuple(winning2.getId(), WinningState.ACTIVE)
            );
    }

    @DisplayName(value = "해당 신청 회차의 당첨 목록을 조회할 때 신청 회차가 다른 경우 조회되지 않는다.")
    @Test
    public void findAllByWinningApplyApplyRoundIdAndTypeAndStateWithOtherApplyRound() {
        // given
        Vehicle vehicle = vehicleRepository.save(createVehicle());

        ApplyRound applyRound1 = applyRoundRepository.save(createApplyRound(vehicle, LocalDate.of(2024, 7, 22)));
        ApplyRound applyRound2 = applyRoundRepository.save(createApplyRound(vehicle, LocalDate.of(2024, 7, 29)));

        Member member1 = memberRepository.save(createMember());
        Member member2 = memberRepository.save(createMember());

        Apply apply1 = applyRepository.save(createApply(member1, applyRound1, vehicle));
        Apply apply2 = applyRepository.save(createApply(member2, applyRound1, vehicle));

        Winning winning1 = createWinning(vehicle, apply1, WinningState.ACTIVE);
        Winning winning2 = createWinning(vehicle, apply2, WinningState.ACTIVE);
        winningRepository.saveAll(List.of(winning1, winning2));

        // when
        List<Winning> winningList = winningRepository.findAllByApplyApplyRoundIdAndState(applyRound2.getId(), WinningState.ACTIVE);

        // then
        assertThat(winningList).isEmpty();
    }

    private Member createMember() {
        return Member.of("employeeNo", "name", "email", "password", LocalDate.of(1999,11,28),
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
        return Apply.of(member, applyRound, vehicle, "departures", "arrivals", ApplyPurpose.EVENT, ApplyState.ACTIVE);
    }

    private Winning createWinning(Vehicle vehicle, Apply apply, WinningState state) {
        return Winning.of(vehicle, apply, state);
    }

    private VehicleReport createVehicleReport(Winning winning) {
        return VehicleReport.of(winning, "frontImg", "leftImg", "rightImg", "backImg", "dashboardImg",
            "parkingLoc", VehicleReportType.TAKE, VehicleReportState.ACTIVE);
    }
}