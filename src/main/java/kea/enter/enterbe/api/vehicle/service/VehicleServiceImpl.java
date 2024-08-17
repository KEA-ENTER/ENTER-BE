package kea.enter.enterbe.api.vehicle.service;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kea.enter.enterbe.api.vehicle.service.dto.PostVehicleReportServiceDto;
import kea.enter.enterbe.domain.apply.entity.ApplyRound;
import kea.enter.enterbe.domain.lottery.entity.Winning;
import kea.enter.enterbe.domain.lottery.entity.WinningState;
import kea.enter.enterbe.domain.lottery.repository.WinningRepository;
import kea.enter.enterbe.domain.penalty.entity.Penalty;
import kea.enter.enterbe.domain.penalty.entity.PenaltyLevel;
import kea.enter.enterbe.domain.penalty.entity.PenaltyReason;
import kea.enter.enterbe.domain.penalty.repository.PenaltyRepository;
import kea.enter.enterbe.domain.take.entity.VehicleReport;
import kea.enter.enterbe.domain.take.entity.VehicleReportPostTime;
import kea.enter.enterbe.domain.take.entity.VehicleReportState;
import kea.enter.enterbe.domain.take.entity.VehicleReportType;
import kea.enter.enterbe.domain.take.repository.VehicleReportRepository;
import kea.enter.enterbe.domain.vehicle.entity.VehicleNote;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import kea.enter.enterbe.domain.vehicle.repository.VehicleNoteRepository;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import kea.enter.enterbe.global.util.ObjectStorageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VehicleServiceImpl implements VehicleService {

    private final ObjectStorageUtil objectStorageUtil;
    private final WinningRepository winningRepository;
    private final VehicleReportRepository vehicleReportRepository;
    private final VehicleNoteRepository vehicleNoteRepository;
    private final PenaltyRepository penaltyRepository;
    private final Clock clock;

    @Transactional
    @Override
    public void postVehicleReport(PostVehicleReportServiceDto dto) {
        List<String> images = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now(clock);
        Winning winning = getWinningByMemberIdThisWeek(dto.getMemberId(), now.toLocalDate());
        if (!checkTakeReturnTime(winning.getApply().getApplyRound(), now)) {
            throw new CustomException(ResponseCode.NOT_REPORT_POST_TIME);
        }
        try {
            String frontImg = uploadS3Image(dto.getFrontImg());
            images.add(frontImg);
            String rightImg = uploadS3Image(dto.getRightImg());
            images.add(rightImg);
            String backImg = uploadS3Image(dto.getBackImg());
            images.add(backImg);
            String leftImg = uploadS3Image(dto.getLeftImg());
            images.add(leftImg);
            String dashboardImg = uploadS3Image(dto.getDashboardImg());
            images.add(dashboardImg);

            VehicleReport vehicleReport = vehicleReportRepository.save(
                VehicleReport.create(winning, winning.getApply().getApplyRound().getVehicle(),
                    frontImg, leftImg,
                    rightImg, backImg, dashboardImg, dto.getParkingLoc(), dto.getType()));

            if (StringUtils.hasText(dto.getNote())) {
                vehicleNoteRepository.save(
                    VehicleNote.create(winning.getApply().getApplyRound().getVehicle(),
                        vehicleReport, dto.getNote()));
            }

        } catch (Exception e) {
            deleteS3Images(images);
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public void checkVehicleReport() {
        List<VehicleReport> vehicleReports;
        int returnReport, takeReport;
        LocalDate now = LocalDate.now(clock);
        List<Winning> winning = winningRepository.findAllByStateAndApplyApplyRoundReturnDate(
            WinningState.ACTIVE, now.minusDays(1));
        for (Winning w : winning) {
            takeReport = 0;
            returnReport = 0;
            vehicleReports = vehicleReportRepository.findAllByWinningIdAndState(w.getId(),
                VehicleReportState.ACTIVE);
            for (VehicleReport vr : vehicleReports) {
                if (vr.getType().equals(VehicleReportType.TAKE)) {
                    takeReport++;
                } else if (vr.getType().equals(VehicleReportType.RETURN)) {
                    returnReport++;
                }
            }
            if (takeReport == 0) {
                w.getApply().getApplyRound().getVehicle().setState(VehicleState.ON_RENT);
                penaltyRepository.save(Penalty.create(w.getApply().getMember(), PenaltyReason.TAKE,
                    PenaltyLevel.MEDIUM, null));
            } else if (returnReport == 0) {
                w.getApply().getApplyRound().getVehicle().setState(VehicleState.AVAILABLE);
                penaltyRepository.save(
                    Penalty.create(w.getApply().getMember(), PenaltyReason.RETURN,
                        PenaltyLevel.MEDIUM, null));
            }
        }
    }

    private boolean checkTakeReturnTime(ApplyRound applyRound, LocalDateTime now) {
        LocalDateTime takeDate = applyRound.getTakeDate().minusDays(1)
            .atTime(VehicleReportPostTime.TAKE_TIME.getTime());
        LocalDateTime returnDate = applyRound.getReturnDate().plusDays(1)
            .atTime(VehicleReportPostTime.RETURN_TIME.getTime());
        //제출
        return now.isAfter(takeDate) && now.isBefore(returnDate);
    }

    private Winning getWinningByMemberIdThisWeek(Long memberId, LocalDate now) {
        return winningRepository.findByApplyMemberIdAndApplyApplyRoundTakeDateBetweenAndState(
                memberId, now.with(DayOfWeek.MONDAY), now.with(DayOfWeek.SUNDAY), WinningState.ACTIVE)
            .orElseThrow(() -> new CustomException(ResponseCode.WINNING_NOT_FOUND));
    }

    private void deleteS3Image(String imageUrl) {
        objectStorageUtil.delete(imageUrl);
    }

    private void deleteS3Images(List<String> images) {
        for (String image : images) {
            deleteS3Image(image);
        }
    }

    private String uploadS3Image(MultipartFile images) {
        return objectStorageUtil.uploadFileToS3(images);
    }
}
