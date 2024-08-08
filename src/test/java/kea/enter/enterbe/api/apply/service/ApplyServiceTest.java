package kea.enter.enterbe.api.apply.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import kea.enter.enterbe.IntegrationTestSupport;
import kea.enter.enterbe.api.apply.controller.dto.response.GetApplyResponse;
import kea.enter.enterbe.api.apply.controller.dto.response.GetApplyVehicleResponse;
import kea.enter.enterbe.api.apply.service.dto.GetApplyServiceDto;
import kea.enter.enterbe.api.apply.service.dto.GetApplyVehicleServiceDto;
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

public class ApplyServiceTest extends IntegrationTestSupport {
    @DisplayName("신청 가능한 날짜를 조회한다.")
    @Test
    void getApply() {
        // given
        LocalDate today  = LocalDate.now();
        LocalDate nextTuesday = today.with(DayOfWeek.TUESDAY).plusDays(7); // 다음 주 화요일
        LocalDate nextThursday = today.with(DayOfWeek.THURSDAY).plusDays(7); // 다음 주 목요일
        LocalDate nextSaturday = today.with(DayOfWeek.SATURDAY).plusDays(7); // 다음 주 토요일
        Vehicle vehicle = vehicleRepository.save(createVehicle());

        // 같은 주에 3개의 신청이 존재 회차는 1회차로 동일, 요일이 다름
        // 다음 주 화
        applyRoundRepository.save(createApplyRound1(vehicle, nextTuesday));
        // 다음 주 목
        applyRoundRepository.save(createApplyRound1(vehicle, nextThursday));
        // 다음 주 토~일
        applyRoundRepository.save(createApplyRound2(vehicle, nextSaturday));

        GetApplyServiceDto dto = GetApplyServiceDto.of(today);

        // when
        List<GetApplyResponse> response = applyService.getApply(dto);


        // then
        assertThat(response)
            .hasSize(3)
            .extracting("takeDate", "returnDate")
            .containsExactlyInAnyOrder(
                tuple(nextTuesday,nextTuesday),
                tuple(nextThursday,nextThursday),
                tuple(nextSaturday,nextSaturday.plusDays(1))
            );

    }
    @DisplayName("신청 가능한 차량의 목록과 경쟁률을 조회한다.")
    @Test
    void getApplyVehicles() {
        // given
        LocalDate today  = LocalDate.now();
        LocalDate nextTuesday = today.with(DayOfWeek.TUESDAY).plusDays(7); // 다음 주 화요일

        Vehicle vehicle1 = vehicleRepository.save(createVehicle());
        Vehicle vehicle2 = vehicleRepository.save(createVehicle());


        // 다음 주 화요일에 다른 차량 2대에 대해서 각각 ApplyRound가 존재
        ApplyRound applyRound1 = applyRoundRepository.save(createApplyRound1(vehicle1, nextTuesday));
        ApplyRound applyRound2 = applyRoundRepository.save(createApplyRound1(vehicle2, nextTuesday));

        // 4명의 사용자
        Member member1 = memberRepository.save(createMember());
        Member member2 = memberRepository.save(createMember());
        Member member3 = memberRepository.save(createMember());
        Member member4 = memberRepository.save(createMember());

        // 3명은 applyRound1, 1명은 applyRound2 지원
        applyRepository.save(createApply(member1, applyRound1));
        applyRepository.save(createApply(member2, applyRound1));
        applyRepository.save(createApply(member3, applyRound1));
        applyRepository.save(createApply(member4, applyRound2));

        // 인수, 반납일이 다음주 화요일인 차량의 목록을 조회
        GetApplyVehicleServiceDto dto = GetApplyVehicleServiceDto.of(nextTuesday, nextTuesday);

        // when
        List<GetApplyVehicleResponse> response = applyService.getApplyVehicles(dto);


        // then
        assertThat(response)
            .hasSize(2)
            .extracting("competition")
            .containsExactlyInAnyOrder(1, 3);

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
    private ApplyRound createApplyRound1(Vehicle vehicle, LocalDate takeDate) {
        return ApplyRound.of(vehicle, 1, takeDate, takeDate, ApplyRoundState.ACTIVE);
    }
    private ApplyRound createApplyRound2(Vehicle vehicle, LocalDate takeDate) {
        return ApplyRound.of(vehicle, 1, takeDate, takeDate.plusDays(1), ApplyRoundState.ACTIVE);
    }
    private Apply createApply(Member member, ApplyRound applyRound) {
        return Apply.of(member, applyRound,  ApplyPurpose.EVENT, ApplyState.ACTIVE);
    }
}
