package kea.enter.enterbe.api.apply.service;

import kea.enter.enterbe.IntegrationTestSupport;
import kea.enter.enterbe.api.apply.controller.response.GetApplySituationResponse;
import kea.enter.enterbe.domain.apply.entity.Apply;
import kea.enter.enterbe.domain.apply.entity.ApplyState;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.entity.MemberRole;
import kea.enter.enterbe.domain.member.entity.MemberState;
import kea.enter.enterbe.domain.apply.entity.ApplyRound;
import kea.enter.enterbe.domain.apply.entity.ApplyRoundState;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
import kea.enter.enterbe.domain.vehicle.entity.VehicleFuel;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import kea.enter.enterbe.domain.lottery.entity.Winning;
import kea.enter.enterbe.domain.lottery.entity.WinningState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class AdminApplyServiceImplTest extends IntegrationTestSupport {
    @DisplayName("이번주의 응모 현황을 조회한다.")
    @Test
    void getApplySituation() {
        // given
        Vehicle vehicle1 = vehicleRepository.save(createVehicle());
        Vehicle vehicle2 = vehicleRepository.save(createVehicle());

        ApplyRound applyRound1 = applyRoundRepository.save(createApplyRound(vehicle1, LocalDate.of(2024, 7, 29)));
        ApplyRound applyRound2 = applyRoundRepository.save(createApplyRound(vehicle2, LocalDate.of(2024, 7, 31)));

        Member member1 = memberRepository.save(createMember());
        Member member2 = memberRepository.save(createMember());

        Apply apply1 = applyRepository.save(createApply(member1, applyRound1, vehicle1));
        Apply apply2 = applyRepository.save(createApply(member2, applyRound2, vehicle2));

        winningRepository.save(createWinning(vehicle1, apply1, WinningState.ACTIVE));
        winningRepository.save(createWinning(vehicle2, apply2, WinningState.INACTIVE));

        // when
        GetApplySituationResponse response = adminApplyService.getApplySituation();

        // then
        assertThat(response)
            .extracting("applyRound", "applyCnt", "winningCnt", "cancelCnt")
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
}