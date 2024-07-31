package kea.enter.enterbe.domain.winning.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Optional;
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

class WinningRepositoryTest extends IntegrationTestSupport {

    @DisplayName(value = "멤버아이디로 이번회차의 당첨 데이터를 조회한다.")
    @Test
    public void findByMemberIdAndTakeDateAndState() throws Exception {
        //given
        LocalDate takeDate = LocalDate.of(1999, 8, 20);
        LocalDate returnDate = LocalDate.of(1999, 8, 30);
        Vehicle vehicle = vehicleRepository.save(createVehicle());
        Member member = memberRepository.save(createMember());
        ApplyRound applyRound = applyRoundRepository.save(
            createApplyRound(vehicle, takeDate, returnDate));
        Apply apply = applyRepository.save(createApply(member, applyRound, vehicle));
        winningRepository.save(createWinning(vehicle, apply));
        //when
        Optional<Winning> savedWinning = winningRepository.findByMemberIdAndTakeDateAndState(
            member.getId(), takeDate, WinningState.ACTIVE);
        //then
        assertThat(savedWinning).isNotNull();
    }

    @DisplayName(value = "멤버아이디로 이번회차의 당첨 데이터를 조회할 때 멤버가 없으면 조회되지 않는다.")
    @Test
    public void findByWinningMemberIdAndTakeDateAndStateWithoutMember() throws Exception {
        //given
        Long wrongMemberId = 999L;
        LocalDate takeDate = LocalDate.of(1999, 8, 20);
        LocalDate returnDate = LocalDate.of(1999, 8, 30);
        Vehicle vehicle = vehicleRepository.save(createVehicle());
        Member member = memberRepository.save(createMember());
        ApplyRound applyRound = applyRoundRepository.save(
            createApplyRound(vehicle, takeDate, returnDate));
        Apply apply = applyRepository.save(createApply(member, applyRound, vehicle));
        winningRepository.save(createWinning(vehicle, apply));
        //when
        Optional<Winning> savedWinning = winningRepository.findByMemberIdAndTakeDateAndState(
            wrongMemberId, takeDate, WinningState.ACTIVE);
        //then
        assertThat(savedWinning).isEmpty();
    }

    @DisplayName(value = "멤버아이디로 이번회차의 당첨 데이터를 조회할 때 인수 날짜와 오늘이 다른 경우 조회되지 않는다.")
    @Test
    public void findWinningByMemberIdAndTakeDateAndStateWithoutMember() throws Exception {
        //given
        LocalDate wrongDate = LocalDate.of(1999, 8, 21);
        LocalDate takeDate = LocalDate.of(1999, 8, 20);
        LocalDate returnDate = LocalDate.of(1999, 8, 30);
        Vehicle vehicle = vehicleRepository.save(createVehicle());
        Member member = memberRepository.save(createMember());
        ApplyRound applyRound = applyRoundRepository.save(
            createApplyRound(vehicle, takeDate, returnDate));
        Apply apply = applyRepository.save(createApply(member, applyRound, vehicle));
        winningRepository.save(createWinning(vehicle, apply));
        //when
        Optional<Winning> savedWinning = winningRepository.findByMemberIdAndTakeDateAndState(
            member.getId(), wrongDate, WinningState.ACTIVE);
        //then
        assertThat(savedWinning).isEmpty();
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