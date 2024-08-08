package kea.enter.enterbe.api.lottery.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import kea.enter.enterbe.IntegrationTestSupport;
import kea.enter.enterbe.api.lottery.controller.dto.request.ApplicantSearchType;
import kea.enter.enterbe.api.lottery.controller.dto.request.LotterySearchType;
import kea.enter.enterbe.api.lottery.controller.dto.response.GetApplicantListResponse;
import kea.enter.enterbe.api.lottery.controller.dto.response.GetLotteryListResponse;
import kea.enter.enterbe.api.lottery.service.dto.GetApplicantListServiceDto;
import kea.enter.enterbe.api.lottery.service.dto.GetLotteryListServiceDto;
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
import org.springframework.data.domain.PageRequest;

class AdminLotteryServiceTest extends IntegrationTestSupport {
    @DisplayName("추첨 관리 목록을 조회한다. (검색, 페이징)")
    @Test
    void getLotteryList() {
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

        GetLotteryListServiceDto dto = GetLotteryListServiceDto.of(null, LotterySearchType.ALL, PageRequest.of(0, 10));

        // when
        GetLotteryListResponse response = adminLotteryService.getLotteryList(dto);

        // then
        assertThat(response.getLotteryList()).hasSize(4)
            .extracting("round")
            .contains(
                applyRound4.getRound(),
                applyRound3.getRound(),
                applyRound2.getRound(),
                applyRound1.getRound()
            );
    }

    @DisplayName("해당 신청 회차의 신청자 목록을 조회한다. (검색, 페이징)")
    @Test
    void getApplicantList() {
        // given
        Vehicle vehicle = vehicleRepository.save(createVehicle());
        ApplyRound applyRound = applyRoundRepository.save(createApplyRound(vehicle, 1, LocalDate.of(2024, 7, 29)));

        Member member1 = memberRepository.save(createMember());
        Member member2 = memberRepository.save(createMember());
        Member member3 = memberRepository.save(createMember());

        Apply winningApply = applyRepository.save(createApply(member1, applyRound));
        applyRepository.save(createApply(member2, applyRound));
        applyRepository.save(createApply(member3, applyRound));

        winningRepository.save(createWinning(winningApply, WinningState.ACTIVE));

        GetApplicantListServiceDto dto = GetApplicantListServiceDto.of(applyRound.getId(), null, ApplicantSearchType.ALL, PageRequest.of(0, 10));

        // when
        GetApplicantListResponse response = adminLotteryService.getApplicantList(dto);

        // then
        assertThat(response.getApplicantList()).hasSize(3)
            .extracting("isWinning")
            .contains(false, false, true);
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

    private ApplyRound createApplyRound(Vehicle vehicle, int applyRound, LocalDate takeDate) {
        return ApplyRound.of(vehicle, applyRound, takeDate, takeDate.plusDays(1), ApplyRoundState.ACTIVE);
    }

    private Apply createApply(Member member, ApplyRound applyRound) {
        return Apply.of(member, applyRound, ApplyPurpose.EVENT, ApplyState.ACTIVE);
    }

    private Winning createWinning(Apply apply, WinningState state) {
        return Winning.of(apply, state);
    }
}