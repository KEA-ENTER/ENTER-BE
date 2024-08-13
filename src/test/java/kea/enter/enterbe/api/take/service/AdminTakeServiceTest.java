package kea.enter.enterbe.api.take.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.time.LocalDate;
import kea.enter.enterbe.IntegrationTestSupport;
import kea.enter.enterbe.api.take.controller.dto.response.GetReturnReportResponse;
import kea.enter.enterbe.api.take.service.dto.GetReturnReportServiceDto;
import kea.enter.enterbe.api.take.controller.dto.response.GetTakeReportResponse;
import kea.enter.enterbe.api.take.controller.dto.response.GetTakeSituationResponse;
import kea.enter.enterbe.api.take.service.dto.GetTakeReportServiceDto;
import kea.enter.enterbe.api.take.service.dto.GetTakeSituationServiceDto;
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
import kea.enter.enterbe.domain.take.entity.VehicleReport;
import kea.enter.enterbe.domain.take.entity.VehicleReportState;
import kea.enter.enterbe.domain.take.entity.VehicleReportType;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
import kea.enter.enterbe.domain.vehicle.entity.VehicleFuel;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AdminTakeServiceTest extends IntegrationTestSupport {
    @DisplayName("저번주의 인수 현황을 조회한다. (성공)")
    @Test
    void getTakeSituation() {
        // given
        LocalDate now  = LocalDate.now();

        LocalDate date = now.minusDays(7);
        LocalDate lastMonday = date.with(DayOfWeek.MONDAY);  // 저번주 월요일
        LocalDate lastSunday = date.with(DayOfWeek.SUNDAY);  // 저번주 일요일

        Vehicle vehicle1 = vehicleRepository.save(createVehicle());
        Vehicle vehicle2 = vehicleRepository.save(createVehicle());

        // 경계값으로 테스트
        ApplyRound applyRound1 = applyRoundRepository.save(createApplyRound(vehicle1, lastMonday));
        ApplyRound applyRound2 = applyRoundRepository.save(createApplyRound(vehicle2, lastSunday));

        Member member1 = memberRepository.save(createMember());
        Member member2 = memberRepository.save(createMember());

        Apply apply1 = applyRepository.save(createApply(member1, applyRound1));
        Apply apply2 = applyRepository.save(createApply(member2, applyRound2));

        Winning winning = winningRepository.save(createWinning(apply1, WinningState.ACTIVE));
        winningRepository.save(createWinning(apply2, WinningState.INACTIVE));

        vehicleReportRepository.save(createVehicleReport(winning, VehicleReportType.TAKE));

        GetTakeSituationServiceDto dto = GetTakeSituationServiceDto.of(now);

        // when
        GetTakeSituationResponse response = adminTakeService.getTakeSituation(dto);

        // then
        assertThat(response)
            .extracting("applyRound", "applyCnt", "takeCnt", "noShowCnt")
            .contains(1, 2, 1, 1);
    }
  
    @DisplayName("해당 당첨자의 차량 인수 보고서를 조회한다. (성공)")
    @Test
    void getTakeReport() {
        // given
        Vehicle vehicle = vehicleRepository.save(createVehicle());
        ApplyRound applyRound = applyRoundRepository.save(createApplyRound(vehicle, LocalDate.of(2024, 7, 29)));

        Member member = memberRepository.save(createMember());
        Apply apply = applyRepository.save(createApply(member, applyRound));
        Winning winning = winningRepository.save(createWinning(apply, WinningState.ACTIVE));
      
        VehicleReport vehicleReport = vehicleReportRepository.save(createVehicleReport(winning, VehicleReportType.TAKE));
        GetTakeReportServiceDto dto = GetTakeReportServiceDto.of(winning.getId());

        // when
        GetTakeReportResponse response = adminTakeService.getTakeReport(dto);
      
        // then
        assertThat(response)
            .extracting("reportId")
            .isEqualTo(vehicleReport.getId());
    }

    @DisplayName("해당 당첨자의 차량 반납 보고서를 조회한다. (성공)")
    @Test
    void getReturnReport() {
        // given
        Vehicle vehicle = vehicleRepository.save(createVehicle());
        ApplyRound applyRound = applyRoundRepository.save(createApplyRound(vehicle, LocalDate.of(2024, 7, 29)));

        Member member = memberRepository.save(createMember());
        Apply apply = applyRepository.save(createApply(member, applyRound));
        Winning winning = winningRepository.save(createWinning(apply, WinningState.ACTIVE));

        VehicleReport vehicleReport = vehicleReportRepository.save(createVehicleReport(winning, VehicleReportType.RETURN));
        GetReturnReportServiceDto dto = GetReturnReportServiceDto.of(winning.getId());

        // when
        GetReturnReportResponse response = adminTakeService.getReturnReport(dto);

        // then
        assertThat(response)
            .extracting("reportId")
            .isEqualTo(vehicleReport.getId());
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

    private VehicleReport createVehicleReport(Winning winning, VehicleReportType type) {
        return VehicleReport.of(winning, winning.getApply().getApplyRound().getVehicle(),"frontImg","leftImg", "rightImg", "backImg", "dashboardImg",
            "parkingLoc", type, VehicleReportState.ACTIVE);
    }
}