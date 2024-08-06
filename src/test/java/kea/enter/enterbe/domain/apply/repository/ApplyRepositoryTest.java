package kea.enter.enterbe.domain.apply.repository;

import kea.enter.enterbe.IntegrationTestSupport;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class ApplyRepositoryTest extends IntegrationTestSupport {
    @DisplayName(value = "해당 신청 회차의 신청 목록을 조회한다.")
    @Test
    public void findAllByApplyRoundIdAndState() {
        // given
        Vehicle vehicle = vehicleRepository.save(createVehicle());

        ApplyRound applyRound = applyRoundRepository.save(createApplyRound(vehicle, LocalDate.of(2024, 7, 29)));

        Member member1 = memberRepository.save(createMember());
        Member member2 = memberRepository.save(createMember());

        Apply apply1 = createApply(member1, applyRound, vehicle);
        Apply apply2 = createApply(member2, applyRound, vehicle);
        applyRepository.saveAll(List.of(apply1, apply2));

        // when
        List<Apply> applyList = applyRepository.findAllByApplyRoundIdAndState(applyRound.getId(), ApplyState.ACTIVE);

        // then
        assertThat(applyList).hasSize(2)
            .extracting("id", "state")
            .containsExactlyInAnyOrder(
                tuple(apply1.getId(), ApplyState.ACTIVE),
                tuple(apply2.getId(), ApplyState.ACTIVE)
            );
    }

    @DisplayName(value = "해당 신청 회차의 신청 목록을 조회할 때 신청 회차가 다른 경우 조회되지 않는다.")
    @Test
    public void findAllByApplyRoundIdAndStateWithOtherApplyRound() {
        // given
        Vehicle vehicle = vehicleRepository.save(createVehicle());

        ApplyRound applyRound1 = applyRoundRepository.save(createApplyRound(vehicle, LocalDate.of(2024, 7, 29)));
        ApplyRound applyRound2 = applyRoundRepository.save(createApplyRound(vehicle, LocalDate.of(2024, 7, 29)));


        Member member1 = memberRepository.save(createMember());
        Member member2 = memberRepository.save(createMember());

        Apply apply1 = createApply(member1, applyRound1, vehicle);
        Apply apply2 = createApply(member2, applyRound1, vehicle);
        applyRepository.saveAll(List.of(apply1, apply2));

        // when
        List<Apply> applyList = applyRepository.findAllByApplyRoundIdAndState(applyRound2.getId(), ApplyState.ACTIVE);

        // then
        assertThat(applyList).isEmpty();
    }

    private Vehicle createVehicle() {
        return Vehicle.of("vehicleNo", "company", "model", 4,
            VehicleFuel.DIESEL, "img", VehicleState.AVAILABLE);
    }

    private ApplyRound createApplyRound(Vehicle vehicle, LocalDate takeDate) {
        return ApplyRound.of(vehicle, 1, takeDate, takeDate.plusDays(1), ApplyRoundState.ACTIVE);
    }

    private Member createMember() {
        return Member.of("employeeNo", "name", "email", "password", LocalDate.of(1999,11,28),
            "licenseId", "licensePassword", true, true,
            1, MemberRole.USER, MemberState.ACTIVE);
    }

    private Apply createApply(Member member, ApplyRound applyRound, Vehicle vehicle) {
        return Apply.of(member, applyRound, vehicle, "departures", "arrivals", ApplyState.ACTIVE);
    }
}