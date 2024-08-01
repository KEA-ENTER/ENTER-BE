package kea.enter.enterbe.api.vehicle.service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import kea.enter.enterbe.api.vehicle.service.dto.PostVehicleReportServiceDto;
import kea.enter.enterbe.domain.vehicle.entity.VehicleNote;
import kea.enter.enterbe.domain.vehicle.repository.VehicleNoteRepository;
import kea.enter.enterbe.domain.take.entity.VehicleReport;
import kea.enter.enterbe.domain.take.entity.VehicleReportType;
import kea.enter.enterbe.domain.take.repository.VehicleReportRepository;
import kea.enter.enterbe.domain.lottery.entity.Winning;
import kea.enter.enterbe.domain.lottery.entity.WinningState;
import kea.enter.enterbe.domain.lottery.repository.WinningRepository;
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
    private final Clock clock;

    @Override
    public void postVehicleReport(PostVehicleReportServiceDto dto) {
        List<String> images = new ArrayList<>();
        Winning winning = getWinningByReportType(dto.getMemberId(), dto.getType());

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
                VehicleReport.create(winning, frontImg, leftImg,
                    rightImg, backImg, dashboardImg, dto.getParkingLoc(), dto.getType()));

            if (StringUtils.hasText(dto.getNote())) {
                vehicleNoteRepository.save(
                    VehicleNote.create(winning.getVehicle(), vehicleReport, dto.getNote()));
            }

        } catch (Exception e) {
            deleteS3Images(images);
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    private Winning getWinningByReportType(Long memberId, VehicleReportType type) {
        if (type.equals(VehicleReportType.TAKE)) {
            return findWinningByMemberIdAndTakeDate(memberId, LocalDate.now(clock));
        } else {
            return findWinningByMemberIdAndReturnDate(memberId, LocalDate.now(clock));
        }
    }

    private Winning findWinningByMemberIdAndReturnDate(Long memberId, LocalDate date) {
        return winningRepository.findByMemberIdAndReturnDateAndState(memberId, date,
                WinningState.ACTIVE)
            .orElseThrow(() -> new CustomException(ResponseCode.WINNING_NOT_FOUND));
    }

    private Winning findWinningByMemberIdAndTakeDate(Long memberId, LocalDate date) {
        return winningRepository.findByMemberIdAndTakeDateAndState(memberId, date,
                WinningState.ACTIVE)
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
