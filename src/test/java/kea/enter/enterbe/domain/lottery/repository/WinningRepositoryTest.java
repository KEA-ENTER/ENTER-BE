package kea.enter.enterbe.domain.lottery.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import kea.enter.enterbe.IntegrationTestSupport;
import kea.enter.enterbe.domain.apply.entity.Apply;
import kea.enter.enterbe.domain.apply.entity.ApplyPurpose;
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
import org.springframework.transaction.annotation.Transactional;

class WinningRepositoryTest extends IntegrationTestSupport {

    @DisplayName(value = "멤버아이디로 이번회차의 당첨 데이터를 조회한다. (성공)")
    @Test
    public void findByMemberIdAndTakeDateAndState() throws Exception {
        //given
        LocalDate takeDate = LocalDate.of(1999, 8, 20);
        LocalDate returnDate = LocalDate.of(1999, 8, 30);
        Vehicle vehicle = vehicleRepository.save(createVehicle());
        Member member = memberRepository.save(createMember());
        ApplyRound applyRound = applyRoundRepository.save(
            createApplyRound(vehicle, takeDate, returnDate));
        Apply apply = applyRepository.save(createApply(member, applyRound));
        winningRepository.save(createWinning(apply));
        //when
        Optional<Winning> savedWinning = winningRepository.findByMemberIdAndTakeDateAndState(
            member.getId(), takeDate, WinningState.ACTIVE);
        //then
        assertThat(savedWinning).isNotNull();
    }

    @DisplayName(value = "이번 회차 당첨 내역 조회 (실패: 멤버 없음)")
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
        Apply apply = applyRepository.save(createApply(member, applyRound));
        winningRepository.save(createWinning(apply));
        //when
        Optional<Winning> savedWinning = winningRepository.findByMemberIdAndTakeDateAndState(
            wrongMemberId, takeDate, WinningState.ACTIVE);
        //then
        assertThat(savedWinning).isEmpty();
    }

    @DisplayName(value = "이번 회차 당첨 내역 조회 (실패: 인수 날짜와 다른 날짜)")
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
        Apply apply = applyRepository.save(createApply(member, applyRound));
        winningRepository.save(createWinning(apply));
        //when
        Optional<Winning> savedWinning = winningRepository.findByMemberIdAndTakeDateAndState(
            member.getId(), wrongDate, WinningState.ACTIVE);
        //then
        assertThat(savedWinning).isEmpty();
    }

    @DisplayName(value = "해당 신청 회차의 당첨자 목록을 조회한다. (성공)")
    @Test
    public void findAllByApplyApplyRoundIdAndState() {
        // given
        Vehicle vehicle = vehicleRepository.save(createVehicle());

        ApplyRound applyRound = applyRoundRepository.save(createApplyRound(vehicle, LocalDate.of(2024, 7, 29), LocalDate.of(2024, 7, 30)));

        Member member1 = memberRepository.save(createMember());
        Member member2 = memberRepository.save(createMember());

        Apply apply1 = createApply(member1, applyRound);
        Apply apply2 = createApply(member2, applyRound);
        applyRepository.saveAll(List.of(apply1, apply2));

        Winning winning = winningRepository.save(createWinning(apply1));

        // when
        List<Winning> winningList = winningRepository.findAllByApplyApplyRoundIdAndState(applyRound.getId(), WinningState.ACTIVE);

        // then
        assertThat(winningList).hasSize(1)
            .extracting("id", "state")
            .containsExactlyInAnyOrder(
                tuple(winning.getId(), WinningState.ACTIVE)
            );
    }

    @Transactional
    @DisplayName(value = "해당 신청 회차의 당첨 취소자 목록을 조회한다. (성공)")
    @Test
    public void findAllByApplyApplyRoundIdAndStateWithInactive() {
        // given
        Vehicle vehicle = vehicleRepository.save(createVehicle());

        ApplyRound applyRound = applyRoundRepository.save(createApplyRound(vehicle, LocalDate.of(2024, 7, 29), LocalDate.of(2024, 7, 30)));

        Member member1 = memberRepository.save(createMember());
        Member member2 = memberRepository.save(createMember());

        Apply apply1 = createApply(member1, applyRound);
        Apply apply2 = createApply(member2, applyRound);
        applyRepository.saveAll(List.of(apply1, apply2));

        Winning winning = winningRepository.save(createWinning(apply1));
        winning.deleteWinning();

        // when
        List<Winning> winningList = winningRepository.findAllByApplyApplyRoundIdAndState(applyRound.getId(), WinningState.INACTIVE);

        // then
        assertThat(winningList).hasSize(1)
            .extracting("id", "state")
            .containsExactlyInAnyOrder(
                tuple(winning.getId(), WinningState.INACTIVE)
            );
    }

    @DisplayName(value = "신청 회차의 당첨자 목록 조회 (실패: 다른 신청 회차)")
    @Test
    public void findAllByApplyRoundIdAndStateWithOtherApplyRound() {
        // given
        Vehicle vehicle = vehicleRepository.save(createVehicle());

        ApplyRound applyRound1 = applyRoundRepository.save(createApplyRound(vehicle, LocalDate.of(2024, 7, 29), LocalDate.of(2024, 7, 30)));
        ApplyRound applyRound2 = applyRoundRepository.save(createApplyRound(vehicle, LocalDate.of(2024, 7, 29), LocalDate.of(2024, 7, 30)));


        Member member1 = memberRepository.save(createMember());
        Member member2 = memberRepository.save(createMember());

        Apply apply1 = createApply(member1, applyRound1);
        Apply apply2 = createApply(member2, applyRound1);
        applyRepository.saveAll(List.of(apply1, apply2));

        // when
        List<Apply> applyList = applyRepository.findAllByApplyRoundIdAndState(applyRound2.getId(), ApplyState.ACTIVE);

        // then
        assertThat(applyList).isEmpty();
    }

    @Transactional
    @DisplayName(value = "해당 신청 내역의 당첨 여부를 조회한다.")
    @Test
    public void findByApplyIdAndState() {
        // given
        Vehicle vehicle = vehicleRepository.save(createVehicle());
        ApplyRound applyRound = applyRoundRepository.save(createApplyRound(vehicle, LocalDate.of(2024, 7, 29), LocalDate.of(2024, 7, 30)));

        Member member = memberRepository.save(createMember());
        Apply apply = applyRepository.save(createApply(member, applyRound));

        winningRepository.save(createWinning(apply));

        // when
        Optional<Winning> winningOptional = winningRepository.findByApplyIdAndState(apply.getId(), WinningState.ACTIVE);

        // then
        assertThat(winningOptional).isPresent();
    }

    private Winning createWinning(Apply apply) {
        return Winning.of(apply, WinningState.ACTIVE);
    }

    private Apply createApply(Member member, ApplyRound applyRound) {
        return Apply.of(member, applyRound, ApplyPurpose.EVENT, ApplyState.ACTIVE);
    }

    private ApplyRound createApplyRound(Vehicle vehicle, LocalDate takeDate, LocalDate returnDate) {
        return ApplyRound.of(vehicle, 1, takeDate, returnDate, ApplyRoundState.ACTIVE);
    }

    private Member createMember() {
        return Member.of("name", "email", "password", LocalDate.of(1999,1,1), "licenseId",
            "licensePassword", true, true, 1, MemberRole.USER, MemberState.ACTIVE);
    }

    private Vehicle createVehicle() {
        return Vehicle.of("vehicleNo", "company", "model", 4, VehicleFuel.DIESEL, "img",
            VehicleState.AVAILABLE);
    }
}