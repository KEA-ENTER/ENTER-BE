package kea.enter.enterbe.api.vehicle.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import kea.enter.enterbe.IntegrationTestSupport;
import kea.enter.enterbe.api.vehicle.service.dto.PostVehicleReportServiceDto;
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
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
class VehicleServiceTest extends IntegrationTestSupport {

    @DisplayName(value = "인수 보고서를 제출한다. (성공)")
    @Test
    public void postTakeVehicleReport() {
        //given
        LocalDateTime now = LocalDateTime.of(1999,8,20,8,59).with(DayOfWeek.SATURDAY);
        LocalDate takeDate = now.toLocalDate().with(DayOfWeek.SATURDAY);
        LocalDate returnDate = now.toLocalDate().with(DayOfWeek.SUNDAY);
        Member member = memberRepository.save(createMember());
        Vehicle vehicle = vehicleRepository.save(createVehicle());
        ApplyRound applyRound = applyRoundRepository.save(
            createApplyRound(vehicle, takeDate, returnDate));
        Apply apply = applyRepository.save(createApply(member, applyRound));
        Winning winning = winningRepository.save(createWinning(apply));

        String note = "note";
        String parkingLoc = null;
        VehicleReportType type = VehicleReportType.TAKE;

        PostVehicleReportServiceDto dto = PostVehicleReportServiceDto.of(member.getId(),
            mock(MultipartFile.class), mock(MultipartFile.class), mock(MultipartFile.class),
            mock(MultipartFile.class), mock(MultipartFile.class), note, parkingLoc, type);
        Clock fixedClock = Clock.fixed(now.toInstant(ZoneOffset.of("+9")), ZoneId.systemDefault());
        willReturn(fixedClock.instant()).given(clock).instant();
        willReturn(fixedClock.getZone()).given(clock).getZone();

        //when
        vehicleService.postVehicleReport(dto);

        //then
        VehicleReport vehicleReport = vehicleReportRepository.findByWinningIdAndState(
            winning.getId(), VehicleReportState.ACTIVE).get(0);
        assertThat(vehicleReport)
            .extracting("type")
            .isEqualTo(VehicleReportType.TAKE);
    }

    @DisplayName(value = "반납 보고서를 제출한다. (성공)")
    @Test
    public void postReturnVehicleReport() {
        //given
        LocalDateTime now = LocalDateTime.of(1999,8,20,8,59).with(DayOfWeek.SUNDAY);
        LocalDate takeDate = now.toLocalDate().with(DayOfWeek.SATURDAY);
        LocalDate returnDate = now.toLocalDate().with(DayOfWeek.SUNDAY);
        Member member = memberRepository.save(createMember());
        Vehicle vehicle = vehicleRepository.save(createVehicle());
        ApplyRound applyRound = applyRoundRepository.save(
            createApplyRound(vehicle, takeDate, returnDate));
        Apply apply = applyRepository.save(createApply(member, applyRound));
        Winning winning = winningRepository.save(createWinning(apply));

        String note = "note";
        String parkingLoc = "parkingLoc";
        VehicleReportType type = VehicleReportType.RETURN;

        PostVehicleReportServiceDto dto = PostVehicleReportServiceDto.of(member.getId(),
            mock(MultipartFile.class), mock(MultipartFile.class), mock(MultipartFile.class),
            mock(MultipartFile.class), mock(MultipartFile.class), note, parkingLoc, type);
        Clock fixedClock = Clock.fixed(now.toInstant(ZoneOffset.of("+9")), ZoneId.systemDefault());
        willReturn(fixedClock.instant()).given(clock).instant();
        willReturn(fixedClock.getZone()).given(clock).getZone();

        //when
        vehicleService.postVehicleReport(dto);

        //then
        VehicleReport vehicleReport = vehicleReportRepository.findByWinningIdAndState(
            winning.getId(), VehicleReportState.ACTIVE).get(0);
        assertThat(vehicleReport)
            .extracting("type")
            .isEqualTo(VehicleReportType.RETURN);
    }

    @DisplayName(value = "인수 보고서를 제출 (실패: 인수 수령일과 맞는 당첨 내역이 없음)")
    @Test
    public void postTakeVehicleReportExceptionWithDifferentTakeDate() {
        //given
        LocalDate takeDate = LocalDate.of(1999, 8, 20);
        LocalDateTime wrongDate = LocalDate.of(1999, 8, 10).atStartOfDay();
        LocalDate returnDate = LocalDate.of(1999, 8, 30);
        Member member = memberRepository.save(createMember());
        Vehicle vehicle = vehicleRepository.save(createVehicle());
        ApplyRound applyRound = applyRoundRepository.save(
            createApplyRound(vehicle, takeDate, returnDate));
        Apply apply = applyRepository.save(createApply(member, applyRound));
        winningRepository.save(createWinning(apply));
        String note = "note";
        String parkingLoc = "parkingLoc";
        VehicleReportType type = VehicleReportType.TAKE;
        PostVehicleReportServiceDto dto = PostVehicleReportServiceDto.of(member.getId(),
            mock(MultipartFile.class), mock(MultipartFile.class), mock(MultipartFile.class),
            mock(MultipartFile.class), mock(MultipartFile.class), note, parkingLoc, type);
        Clock fixedClock = Clock.fixed(wrongDate.toInstant(ZoneOffset.of("+9")), ZoneId.systemDefault());
        willReturn(fixedClock.instant()).given(clock).instant();
        willReturn(fixedClock.getZone()).given(clock).getZone();

        //when then
        assertThatThrownBy(() -> vehicleService.postVehicleReport(dto))
            .isInstanceOf(CustomException.class)
            .extracting("responseCode")
            .isEqualTo(ResponseCode.WINNING_NOT_FOUND);
    }

    @DisplayName(value = "반납 보고서를 제출 (실패: 반납일과 맞는 당첨 내역이 없음)")
    @Test
    public void postReturnVehicleReportExceptionWithDifferentReturnDate() {
        //given
        LocalDate takeDate = LocalDate.of(1999, 8, 20);
        LocalDate returnDate = LocalDate.of(1999, 8, 30);
        LocalDate wrongDate = LocalDate.of(1999, 9, 10);
        Member member = memberRepository.save(createMember());
        Vehicle vehicle = vehicleRepository.save(createVehicle());
        ApplyRound applyRound = applyRoundRepository.save(
            createApplyRound(vehicle, takeDate, returnDate));
        Apply apply = applyRepository.save(createApply(member, applyRound));
        winningRepository.save(createWinning(apply));
        String note = "note";
        String parkingLoc = "parkingLoc";
        VehicleReportType type = VehicleReportType.RETURN;
        PostVehicleReportServiceDto dto = PostVehicleReportServiceDto.of(member.getId(),
            mock(MultipartFile.class), mock(MultipartFile.class), mock(MultipartFile.class),
            mock(MultipartFile.class), mock(MultipartFile.class), note, parkingLoc, type);
        Clock fixedClock = Clock.fixed(wrongDate.atStartOfDay(ZoneId.systemDefault()).toInstant(),
            ZoneId.systemDefault());
        willReturn(fixedClock.instant()).given(clock).instant();
        willReturn(fixedClock.getZone()).given(clock).getZone();

        //when then
        assertThatThrownBy(() -> vehicleService.postVehicleReport(dto))
            .isInstanceOf(CustomException.class)
            .extracting("responseCode")
            .isEqualTo(ResponseCode.WINNING_NOT_FOUND);
    }

    @DisplayName(value = "보고서 제출 실패 (성공: 제출한 이미지 삭제)")
    @Test
    public void postVehicleReportHasExceptionThenDeleteImage() {
        //given
        LocalDate takeDate = LocalDate.of(1999, 8, 20);
        LocalDate returnDate = LocalDate.of(1999, 8, 30);
        Member member = memberRepository.save(createMember());
        Vehicle vehicle = vehicleRepository.save(createVehicle());
        ApplyRound applyRound = applyRoundRepository.save(
            createApplyRound(vehicle, takeDate, returnDate));
        Apply apply = applyRepository.save(createApply(member, applyRound));
        winningRepository.save(createWinning(apply));
        String note = "note";
        String parkingLoc = "parkingLoc";
        VehicleReportType type = VehicleReportType.TAKE;
        PostVehicleReportServiceDto dto = PostVehicleReportServiceDto.of(member.getId(),
            mock(MultipartFile.class), mock(MultipartFile.class), mock(MultipartFile.class),
            mock(MultipartFile.class), mock(MultipartFile.class), note, parkingLoc, type);
        Clock fixedClock = Clock.fixed(takeDate.atStartOfDay(ZoneId.systemDefault()).toInstant(),
            ZoneId.systemDefault());
        willReturn(fixedClock.instant()).given(clock).instant();
        willReturn(fixedClock.getZone()).given(clock).getZone();
        given(objectStorageUtil.uploadFileToS3(any(MultipartFile.class)))
            .willReturn("frontImgUrl") // 첫 번째 호출
            .willReturn("rightImgUrl") // 두 번째 호출
            .willThrow(new RuntimeException("S3 upload failed")) // 세 번째 호출
            .willReturn("leftImgUrl") // 네 번째 호출 (실행되지 않음)
            .willReturn("dashboardImgUrl"); // 다섯 번째 호출 (실행되지 않음)

        //when then
        assertThatThrownBy(() -> vehicleService.postVehicleReport(dto))
            .isInstanceOf(CustomException.class)
            .extracting("responseCode")
            .isEqualTo(ResponseCode.INTERNAL_SERVER_ERROR);
        verify(objectStorageUtil, times(3)).uploadFileToS3(any(MultipartFile.class));
        verify(objectStorageUtil, times(2)).delete(any(String.class));
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
        return Member.of("name", "email", "password", LocalDate.of(1999,11,28) , "licenseId",
            "licensePassword", true, true, 1, MemberRole.USER, MemberState.ACTIVE);
    }

    private Vehicle createVehicle() {
        return Vehicle.of("vehicleNo", "company", "model", 4, VehicleFuel.DIESEL, "img",
            VehicleState.AVAILABLE);
    }

}