package kea.enter.enterbe.api.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willReturn;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import kea.enter.enterbe.IntegrationTestSupport;
import kea.enter.enterbe.api.member.controller.dto.response.GetRoutingResponse;
import kea.enter.enterbe.domain.apply.entity.Apply;
import kea.enter.enterbe.domain.apply.entity.ApplyPurpose;
import kea.enter.enterbe.domain.apply.entity.ApplyRound;
import kea.enter.enterbe.domain.apply.entity.ApplyRoundState;
import kea.enter.enterbe.domain.apply.entity.ApplyState;
import kea.enter.enterbe.domain.lottery.entity.Waiting;
import kea.enter.enterbe.domain.lottery.entity.WaitingState;
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

class MemberServiceImplTest extends IntegrationTestSupport {

    @DisplayName("신청 기간(월요일 09:00:00 ~ 화요일 23:59:59)에 사용자의 상태에 따라서 신청 메뉴의 라우팅 번호를 반환한다. (성공 : 신청자)")
    @Test
    void getApplicantRoutingInformation() {
        //given
        Member member = memberRepository.save(createMember());
        Vehicle vehicle = vehicleRepository.save(createVehicle());
        ApplyRound applyRound = applyRoundRepository.save(createApplyRound(vehicle, LocalDate.of(2024,8,5)));
        Apply apply = applyRepository.save(createApply(member, applyRound));
        // 월요일 오전 9시 1초로 고정
        LocalDateTime now = LocalDateTime.of(2024,8,12,9,0,1);
        Clock fixedClock = Clock.fixed(now.toInstant(ZoneOffset.of("+9")), ZoneId.systemDefault());
        willReturn(fixedClock.instant()).given(clock).instant();
        willReturn(fixedClock.getZone()).given(clock).getZone();

        Long memberId = member.getId();
        //when
        GetRoutingResponse response = memberService.getRoutingInformation(memberId);

        //then
        assertThat(response)
            .extracting("routingId", "userState")
            .containsExactly(2, "APPLICANT");
    }

    @DisplayName("신청 기간(월요일 09:00:00 ~ 화요일 23:59:59)에 사용자의 상태에 따라서 신청 메뉴의 라우팅 번호를 반환한다. (성공 : 사원)")
    @Test
    void getEmployeeRoutingInformation() {
        //given
        Member member = memberRepository.save(createMember());
        Vehicle vehicle = vehicleRepository.save(createVehicle());
        ApplyRound applyRound = applyRoundRepository.save(createApplyRound(vehicle, LocalDate.of(2024,8,5)));
        // 월요일 오전 9시 1초로 고정
        LocalDateTime now = LocalDateTime.of(2024,8,12,9,0,1);
        Clock fixedClock = Clock.fixed(now.toInstant(ZoneOffset.of("+9")), ZoneId.systemDefault());
        willReturn(fixedClock.instant()).given(clock).instant();
        willReturn(fixedClock.getZone()).given(clock).getZone();

        Long memberId = member.getId();
        //when
        GetRoutingResponse response = memberService.getRoutingInformation(memberId);

        //then
        assertThat(response)
            .extracting("routingId", "userState")
            .containsExactly(1, "EMPLOYEE");
    }

    @DisplayName("신청서 내역 조회만 가능한 기간(수요일 00:00:00 ~ 수요일 10:00:00)에 사용자의 상태에 따라서 신청 메뉴의 라우팅 번호를 반환한다. (성공 : 신청자)")
    @Test
    void getApplicantRoutingInformationWhenOnlyView() {
        //given
        Member member = memberRepository.save(createMember());
        Vehicle vehicle = vehicleRepository.save(createVehicle());
        ApplyRound applyRound = applyRoundRepository.save(createApplyRound(vehicle, LocalDate.of(2024,8,5)));
        Apply apply = applyRepository.save(createApply(member, applyRound));
        // 수요일 오전 0시 1초로 고정
        LocalDateTime now = LocalDateTime.of(2024,8,14,0,0,1);
        Clock fixedClock = Clock.fixed(now.toInstant(ZoneOffset.of("+9")), ZoneId.systemDefault());
        willReturn(fixedClock.instant()).given(clock).instant();
        willReturn(fixedClock.getZone()).given(clock).getZone();

        Long memberId = member.getId();
        //when
        GetRoutingResponse response = memberService.getRoutingInformation(memberId);

        //then
        assertThat(response)
            .extracting("routingId", "userState")
            .containsExactly(2, "APPLICANT");
    }

    @DisplayName("신청서 내역 조회만 가능한 기간(수요일 00:00:00 ~ 수요일 10:00:00)에 사용자의 상태에 따라서 신청 메뉴의 라우팅 번호를 반환한다. (성공 : 사원)")
    @Test
    void getEmployeeRoutingInformationWhenOnlyView() {
        //given
        Member member = memberRepository.save(createMember());
        Vehicle vehicle = vehicleRepository.save(createVehicle());
        ApplyRound applyRound = applyRoundRepository.save(createApplyRound(vehicle, LocalDate.of(2024,8,5)));
        // 수요일 오전 0시 1초로 고정
        LocalDateTime now = LocalDateTime.of(2024,8,14,0,0,1);
        Clock fixedClock = Clock.fixed(now.toInstant(ZoneOffset.of("+9")), ZoneId.systemDefault());
        willReturn(fixedClock.instant()).given(clock).instant();
        willReturn(fixedClock.getZone()).given(clock).getZone();

        Long memberId = member.getId();
        //when
        GetRoutingResponse response = memberService.getRoutingInformation(memberId);

        //then
        assertThat(response)
            .extracting("routingId", "userState")
            .containsExactly(3, "EMPLOYEE");
    }

    @DisplayName("당첨 결과 발표 이후(수요일 10:00:00 ~ 일요일 23:59:59) 사용자의 상태에 따라서 신청 메뉴의 라우팅 번호를 반환한다. (성공 : 당첨자)")
    @Test
    void getWinnerRoutingInformation() {
        //given
        Member member = memberRepository.save(createMember());
        Vehicle vehicle = vehicleRepository.save(createVehicle());
        ApplyRound applyRound = applyRoundRepository.save(createApplyRound(vehicle, LocalDate.of(2024,8,5)));
        Apply apply = applyRepository.save(createApply(member, applyRound));
        Winning winning = winningRepository.save(createWinning(apply, WinningState.ACTIVE));
        // 수요일 오전 10시 1초로 고정
        LocalDateTime now = LocalDateTime.of(2024,8,14,10,0,1);
        Clock fixedClock = Clock.fixed(now.toInstant(ZoneOffset.of("+9")), ZoneId.systemDefault());
        willReturn(fixedClock.instant()).given(clock).instant();
        willReturn(fixedClock.getZone()).given(clock).getZone();

        Long memberId = member.getId();
        //when
        GetRoutingResponse response = memberService.getRoutingInformation(memberId);

        //then
        assertThat(response)
            .extracting("routingId", "userState")
            .containsExactly(4, "WINNER");
    }

    @DisplayName("당첨 결과 발표 이후(수요일 10:00:00 ~ 일요일 23:59:59) 사용자의 상태에 따라서 신청 메뉴의 라우팅 번호를 반환한다. (성공 : 대기자)")
    @Test
    void getCandidateRoutingInformation() {
        //given
        Member member = memberRepository.save(createMember());
        Vehicle vehicle = vehicleRepository.save(createVehicle());
        ApplyRound applyRound = applyRoundRepository.save(createApplyRound(vehicle, LocalDate.of(2024,8,5)));
        Apply apply = applyRepository.save(createApply(member, applyRound));
        Waiting waiting = waitingRepository.save(createWaiting(apply, 1, WaitingState.ACTIVE));
        // 수요일 오전 10시 1초로 고정
        LocalDateTime now = LocalDateTime.of(2024,8,14,10,0,1);
        Clock fixedClock = Clock.fixed(now.toInstant(ZoneOffset.of("+9")), ZoneId.systemDefault());
        willReturn(fixedClock.instant()).given(clock).instant();
        willReturn(fixedClock.getZone()).given(clock).getZone();

        Long memberId = member.getId();
        //when
        GetRoutingResponse response = memberService.getRoutingInformation(memberId);

        //then
        assertThat(response)
            .extracting("routingId", "userState")
            .containsExactly(4, "CANDIDATE");
    }

    @DisplayName("당첨 결과 발표 이후(수요일 10:00:00 ~ 일요일 23:59:59) 사용자의 상태에 따라서 신청 메뉴의 라우팅 번호를 반환한다. (성공 : 미당첨자)")
    @Test
    void getNonWinnerRoutingInformation() {
        //given
        Member member = memberRepository.save(createMember());
        Vehicle vehicle = vehicleRepository.save(createVehicle());
        ApplyRound applyRound = applyRoundRepository.save(createApplyRound(vehicle, LocalDate.of(2024,8,5)));
        Apply apply = applyRepository.save(createApply(member, applyRound));
        // 수요일 오전 10시 1초로 고정
        LocalDateTime now = LocalDateTime.of(2024,8,14,10,0,1);
        Clock fixedClock = Clock.fixed(now.toInstant(ZoneOffset.of("+9")), ZoneId.systemDefault());
        willReturn(fixedClock.instant()).given(clock).instant();
        willReturn(fixedClock.getZone()).given(clock).getZone();

        Long memberId = member.getId();
        //when
        GetRoutingResponse response = memberService.getRoutingInformation(memberId);

        //then
        assertThat(response)
            .extracting("routingId", "userState")
            .containsExactly(4, "NON_WINNER");
    }

    @DisplayName("당첨 결과 발표 이후(수요일 10:00:00 ~ 일요일 23:59:59) 사용자의 상태에 따라서 신청 메뉴의 라우팅 번호를 반환한다. (성공 : 사원)")
    @Test
    void getEmployeeRoutingInformationAfterLottery() {
        //given
        Member member = memberRepository.save(createMember());
        Vehicle vehicle = vehicleRepository.save(createVehicle());
        ApplyRound applyRound = applyRoundRepository.save(createApplyRound(vehicle, LocalDate.of(2024,8,5)));
        // 수요일 오전 10시 1초로 고정
        LocalDateTime now = LocalDateTime.of(2024,8,14,10,0,1);
        Clock fixedClock = Clock.fixed(now.toInstant(ZoneOffset.of("+9")), ZoneId.systemDefault());
        willReturn(fixedClock.instant()).given(clock).instant();
        willReturn(fixedClock.getZone()).given(clock).getZone();

        Long memberId = member.getId();
        //when
        GetRoutingResponse response = memberService.getRoutingInformation(memberId);

        //then
        assertThat(response)
            .extracting("routingId", "userState")
            .containsExactly(3, "EMPLOYEE");
    }

    @DisplayName("회차에 포함되지 않는 기간(월요일 00:00:00 ~ 월요일 08:59:59) 사용자의 상태에 따라서 신청 메뉴의 라우팅 번호를 반환한다. (성공 : 사원)")
    @Test
    void getNothingToDoRoutingInformation() {
        //given
        Member member = memberRepository.save(createMember());
        Vehicle vehicle = vehicleRepository.save(createVehicle());
        // 월요일 오전 0시 1초로 고정
        LocalDateTime now = LocalDateTime.of(2024,8,12,0,0,1);
        Clock fixedClock = Clock.fixed(now.toInstant(ZoneOffset.of("+9")), ZoneId.systemDefault());
        willReturn(fixedClock.instant()).given(clock).instant();
        willReturn(fixedClock.getZone()).given(clock).getZone();

        Long memberId = member.getId();
        //when
        GetRoutingResponse response = memberService.getRoutingInformation(memberId);

        //then
        assertThat(response)
            .extracting("routingId", "userState")
            .containsExactly(3, "EMPLOYEE");
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

    private Waiting createWaiting(Apply apply, int waitingNo ,WaitingState state) {
        return Waiting.of(apply, waitingNo ,state);
    }
}