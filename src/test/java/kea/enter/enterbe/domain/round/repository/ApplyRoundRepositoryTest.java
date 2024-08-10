package kea.enter.enterbe.domain.round.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import kea.enter.enterbe.IntegrationTestSupport;
import kea.enter.enterbe.api.lottery.controller.dto.request.LotterySearchType;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

class ApplyRoundRepositoryTest extends IntegrationTestSupport {
    @DisplayName(value = "해당 주에 진행하는 신청 회차 목록을 조회한다.")
    @Test
    public void findAllByTakeDateBetweenAndState() {
        // given
        Vehicle vehicle1 = vehicleRepository.save(createVehicle());
        Vehicle vehicle2 = vehicleRepository.save(createVehicle());

        ApplyRound applyRound1 = createApplyRound(vehicle1, LocalDate.of(2024, 7, 29));
        ApplyRound applyRound2 = createApplyRound(vehicle2, LocalDate.of(2024, 8, 4));
        applyRoundRepository.saveAll(List.of(applyRound1, applyRound2));

        // when
        List<ApplyRound> applyRoundList = applyRoundRepository.findAllByTakeDateBetweenAndState(LocalDate.of(2024, 7, 29), LocalDate.of(2024, 8, 4), ApplyRoundState.ACTIVE);

        // then
        assertThat(applyRoundList).hasSize(2)
            .extracting("id", "state")
            .containsExactlyInAnyOrder(
                tuple(applyRound1.getId(), ApplyRoundState.ACTIVE),
                tuple(applyRound2.getId(), ApplyRoundState.ACTIVE)
            );
    }

    @DisplayName(value = "해당 주에 진행하는 신청 회차 목록이 아니라면 조회되지 않는다.")
    @Test
    public void findAllByTakeDateBetweenAndStateWithDifferentRangeOfTakeDate() {
        // given
        LocalDate now = LocalDate.of(2024, 7, 31);
        LocalDate thisMonday = now.with(DayOfWeek.MONDAY);  // 해당주 월요일
        LocalDate thisSunday = thisMonday.plusDays(6);  // 해당주 일요일

        Vehicle vehicle1 = vehicleRepository.save(createVehicle());
        Vehicle vehicle2 = vehicleRepository.save(createVehicle());

        ApplyRound applyRound1 = createApplyRound(vehicle1, LocalDate.of(2024, 6, 1));
        ApplyRound applyRound2 = createApplyRound(vehicle2, LocalDate.of(2024, 7, 29));
        applyRoundRepository.saveAll(List.of(applyRound1, applyRound2));

        // when
        List<ApplyRound> applyRoundList = applyRoundRepository.findAllByTakeDateBetweenAndState(thisMonday, thisSunday, ApplyRoundState.ACTIVE);

        // then
        assertThat(applyRoundList).hasSize(1)
            .extracting("id", "takeDate", "state")
            .containsExactlyInAnyOrder(
                tuple(applyRound2.getId(), LocalDate.of(2024, 7, 29), ApplyRoundState.ACTIVE)
            );
    }

    @DisplayName("추첨 관리 목록을 최근 회차 순으로 조회한다. (검색, 페이징)")
    @Test
    void findAllApplyRoundByCondition() {
        // given
        Vehicle vehicle1 = vehicleRepository.save(createVehicle());
        Vehicle vehicle2 = vehicleRepository.save(createVehicle());

        ApplyRound applyRound1 = applyRoundRepository.save(createApplyRound(vehicle1, 1, LocalDate.of(2024, 7, 29)));
        ApplyRound applyRound2 = applyRoundRepository.save(createApplyRound(vehicle2, 1, LocalDate.of(2024, 7, 29)));

        Member member1 = memberRepository.save(createMember());
        Member member2 = memberRepository.save(createMember());

        Apply apply1 = applyRepository.save(createApply(member1, applyRound1));
        Apply apply2 = applyRepository.save(createApply(member2, applyRound2));

        winningRepository.save(createWinning(apply1, WinningState.ACTIVE));
        winningRepository.save(createWinning(apply2, WinningState.INACTIVE));

        ApplyRound applyRound3 = applyRoundRepository.save(createApplyRound(vehicle1, 2, LocalDate.of(2024, 8, 5)));
        ApplyRound applyRound4 = applyRoundRepository.save(createApplyRound(vehicle2, 2, LocalDate.of(2024, 8, 7)));

        // when
        Page<ApplyRound> applyRoundList = applyRoundRepository.findAllApplyRoundByCondition(null, LotterySearchType.ALL, PageRequest.of(0, 8));

        // then
        assertThat(applyRoundList).hasSize(4)
            .extracting("round")
            .contains(
                applyRound4.getRound(),
                applyRound3.getRound(),
                applyRound2.getRound(),
                applyRound1.getRound()
            );
    }

    @DisplayName("추첨 관리 목록 차량 검색 시 키워드에 맞는 추첨 회차만 조회한다.")
    @Test
    void findAllApplyRoundByConditionWithVehicleKeyword() {
        // given
        Vehicle vehicle1 = vehicleRepository.save(createVehicle("G80"));
        Vehicle vehicle2 = vehicleRepository.save(createVehicle("G90"));

        ApplyRound applyRound1 = applyRoundRepository.save(createApplyRound(vehicle1, 1, LocalDate.of(2024, 7, 29)));
        ApplyRound applyRound2 = applyRoundRepository.save(createApplyRound(vehicle2, 1, LocalDate.of(2024, 7, 29)));

        Member member1 = memberRepository.save(createMember());
        Member member2 = memberRepository.save(createMember());

        Apply apply1 = applyRepository.save(createApply(member1, applyRound1));
        Apply apply2 = applyRepository.save(createApply(member2, applyRound2));

        winningRepository.save(createWinning(apply1, WinningState.ACTIVE));
        winningRepository.save(createWinning(apply2, WinningState.INACTIVE));

        applyRoundRepository.save(createApplyRound(vehicle1, 2, LocalDate.of(2024, 8, 5)));
        applyRoundRepository.save(createApplyRound(vehicle2, 2, LocalDate.of(2024, 8, 7)));

        // when
        Page<ApplyRound> applyRoundList = applyRoundRepository.findAllApplyRoundByCondition("G80", LotterySearchType.VEHICLE, PageRequest.of(0, 8));

        // then
        assertThat(applyRoundList).hasSize(2)
            .extracting("vehicle.model")
            .contains("G80", "G80");
    }

    @DisplayName("추첨 관리 목록 회차 검색 시 키워드에 맞는 추첨 회차만 조회한다.")
    @Test
    void findAllApplyRoundByConditionWithApplyRoundKeyword() {
        // given
        Vehicle vehicle1 = vehicleRepository.save(createVehicle());
        Vehicle vehicle2 = vehicleRepository.save(createVehicle());

        ApplyRound applyRound1 = applyRoundRepository.save(createApplyRound(vehicle1, 1, LocalDate.of(2024, 7, 29)));
        ApplyRound applyRound2 = applyRoundRepository.save(createApplyRound(vehicle2, 1, LocalDate.of(2024, 7, 29)));

        Member member1 = memberRepository.save(createMember());
        Member member2 = memberRepository.save(createMember());

        Apply apply1 = applyRepository.save(createApply(member1, applyRound1));
        Apply apply2 = applyRepository.save(createApply(member2, applyRound2));

        winningRepository.save(createWinning(apply1, WinningState.ACTIVE));
        winningRepository.save(createWinning(apply2, WinningState.INACTIVE));

        applyRoundRepository.save(createApplyRound(vehicle1, 2, LocalDate.of(2024, 8, 5)));
        applyRoundRepository.save(createApplyRound(vehicle2, 2, LocalDate.of(2024, 8, 7)));

        // when
        Page<ApplyRound> applyRoundList = applyRoundRepository.findAllApplyRoundByCondition("2", LotterySearchType.ROUND, PageRequest.of(0, 8));

        // then
        assertThat(applyRoundList).hasSize(2)
            .extracting("round")
            .contains(2, 2);
    }

    @DisplayName(value = "신청 회차 아이디를 통해 ACTIVE 상태인 신청 회차를 조회한다.")
    @Test
    public void findByIdAndState() {
        // given
        Vehicle vehicle = vehicleRepository.save(createVehicle());
        ApplyRound applyRound = applyRoundRepository.save(createApplyRound(vehicle, LocalDate.of(2024, 7, 29)));

        // when
        Optional<ApplyRound> applyRoundResult = applyRoundRepository.findByIdAndState(applyRound.getId(), ApplyRoundState.ACTIVE);

        // then
        assertThat(applyRoundResult).isPresent();
    }

    @DisplayName(value = "해당 신청 회차가 INACTIVE인 경우 조회되지 않는다.")
    @Test
    public void findByIdAndStateWithInactive() {
        // given
        Vehicle vehicle = vehicleRepository.save(createVehicle());
        ApplyRound applyRound = applyRoundRepository.save(createInactiveApplyRound(vehicle, LocalDate.of(2024, 7, 29)));

        // when
        Optional<ApplyRound> applyRoundResult = applyRoundRepository.findByIdAndState(applyRound.getId(), ApplyRoundState.ACTIVE);

        // then
        assertThat(applyRoundResult).isEmpty();
    }

    private Member createMember() {
        return Member.of("name", "email", "password", LocalDate.of(1999,11,28),
            "licenseId", "licensePassword", true, true,
            1, MemberRole.USER, MemberState.ACTIVE);
    }

    private ApplyRound createApplyRound(Vehicle vehicle, int applyRound, LocalDate takeDate) {
        return ApplyRound.of(vehicle, applyRound, takeDate, takeDate.plusDays(1), ApplyRoundState.ACTIVE);
    }

    private Apply createApply(Member member, ApplyRound applyRound) {
        return Apply.of(member, applyRound, ApplyPurpose.EVENT, ApplyState.ACTIVE);
    }

    private Winning createWinning(Apply apply, WinningState state) {
        return Winning.of(apply, state);
    }

    private Vehicle createVehicle() {
        return Vehicle.of("vehicleNo", "company", "model", 4,
            VehicleFuel.DIESEL, "img", VehicleState.AVAILABLE);
    }

    private Vehicle createVehicle(String model) {
        return Vehicle.of("vehicleNo", "company", model, 4,
            VehicleFuel.DIESEL, "img", VehicleState.AVAILABLE);
    }

    private ApplyRound createApplyRound(Vehicle vehicle, LocalDate takeDate) {
        return ApplyRound.of(vehicle, 1, takeDate, takeDate.plusDays(1), ApplyRoundState.ACTIVE);
    }

    private ApplyRound createInactiveApplyRound(Vehicle vehicle, LocalDate takeDate) {
        return ApplyRound.of(vehicle, 1, takeDate, takeDate.plusDays(1), ApplyRoundState.INACTIVE);
    }
}