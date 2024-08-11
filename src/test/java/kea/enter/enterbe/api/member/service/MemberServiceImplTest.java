package kea.enter.enterbe.api.member.service;

import kea.enter.enterbe.IntegrationTestSupport;
import kea.enter.enterbe.api.member.controller.dto.response.GetRoutingResponse;
import kea.enter.enterbe.domain.apply.entity.Apply;
import kea.enter.enterbe.domain.apply.entity.ApplyPurpose;
import kea.enter.enterbe.domain.apply.entity.ApplyRound;
import kea.enter.enterbe.domain.apply.entity.ApplyRoundState;
import kea.enter.enterbe.domain.apply.entity.ApplyState;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.entity.MemberRole;
import kea.enter.enterbe.domain.member.entity.MemberState;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
import kea.enter.enterbe.domain.vehicle.entity.VehicleFuel;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

class MemberServiceImplTest extends IntegrationTestSupport {

    @DisplayName("신청 기간에 신청자인지 아직 사원 상태인지 확인한다. (성공 : 신청자)")
    @Test
    void getApplicantRoutingInformation() {
        //given
        Member member = memberRepository.save(createMember());
        Vehicle vehicle = vehicleRepository.save(createVehicle());
        ApplyRound applyRound = applyRoundRepository.save(createApplyRound(vehicle, LocalDate.of(2024,8,11)));
        Apply apply = applyRepository.save(createApply(member, applyRound));
        // 월요일 11시로 고정
        given(clock.instant()).willReturn(Instant.parse("2024-08-05T11:00:00Z"));
        given(clock.getZone()).willReturn(ZoneId.systemDefault());

        Long memberId = member.getId();
        //when
        GetRoutingResponse response = memberService.getRoutingInformation(memberId);

        //then
        assertThat(response)
            .extracting("routingId", "userState")
            .containsExactly(2, "APPLICANT");
    }

    @DisplayName("신청 기간에 신청자인지 아직 사원 상태인지 확인한다. (성공 : 지원자)")
    @Test
    void getEmployeeRoutingInformation() {
        //given
        Member member = memberRepository.save(createMember());
        Vehicle vehicle = vehicleRepository.save(createVehicle());
        ApplyRound applyRound = applyRoundRepository.save(createApplyRound(vehicle, LocalDate.of(2024,8,11)));
        // 월요일 11시로 고정
        given(clock.instant()).willReturn(Instant.parse("2024-08-05T11:00:00Z"));
        given(clock.getZone()).willReturn(ZoneId.systemDefault());

        Long memberId = member.getId();
        //when
        GetRoutingResponse response = memberService.getRoutingInformation(memberId);

        //then
        assertThat(response)
            .extracting("routingId", "userState")
            .containsExactly(1, "EMPLOYEE");
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
}