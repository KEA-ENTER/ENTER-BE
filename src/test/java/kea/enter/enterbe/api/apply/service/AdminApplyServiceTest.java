package kea.enter.enterbe.api.apply.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.time.LocalDate;
import kea.enter.enterbe.IntegrationTestSupport;
import kea.enter.enterbe.api.apply.controller.dto.response.GetApplySituationResponse;
import kea.enter.enterbe.api.apply.service.dto.GetApplySituationServiceDto;
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
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
import kea.enter.enterbe.domain.vehicle.entity.VehicleFuel;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AdminApplyServiceTest extends IntegrationTestSupport {
    @DisplayName("이번주의 응모 현황을 조회한다.")
    @Test
    void getApplySituation() {
        // given
        LocalDate now  = LocalDate.now();
        LocalDate lastMonday = now.with(DayOfWeek.MONDAY);  // 이번주 월요일
        LocalDate lastSunday = now.with(DayOfWeek.SUNDAY);  // 이번주 일요일

        Vehicle vehicle1 = vehicleRepository.save(createVehicle());
        Vehicle vehicle2 = vehicleRepository.save(createVehicle());

        ApplyRound applyRound1 = applyRoundRepository.save(createApplyRound(vehicle1, lastMonday));
        ApplyRound applyRound2 = applyRoundRepository.save(createApplyRound(vehicle2, lastSunday));

        Member member1 = memberRepository.save(createMember());
        Member member2 = memberRepository.save(createMember());

        Apply apply1 = applyRepository.save(createApply(member1, applyRound1));
        Apply apply2 = applyRepository.save(createApply(member2, applyRound2));

        winningRepository.save(createWinning(apply1, WinningState.ACTIVE));
        winningRepository.save(createWinning(apply2, WinningState.INACTIVE));

        GetApplySituationServiceDto dto = GetApplySituationServiceDto.of(now);

        // when
        GetApplySituationResponse response = adminApplyService.getApplySituation(dto);

        // then
        assertThat(response)
            .extracting("round", "applyCnt", "winningCnt", "cancelCnt")
            .contains(1, 2, 1, 1);
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

    private Apply createApply(Member member, ApplyRound applyRound) {
        return Apply.of(member, applyRound, ApplyPurpose.EVENT, ApplyState.ACTIVE);
    }

    private Winning createWinning(Apply apply, WinningState state) {
        return Winning.of(apply, state);
    }
}