package kea.enter.enterbe.api.vehicle.service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import kea.enter.enterbe.api.vehicle.service.dto.PostTakeVehicleReportServiceDto;
import kea.enter.enterbe.domain.note.entity.VehicleNote;
import kea.enter.enterbe.domain.note.repository.VehicleNoteRepository;
import kea.enter.enterbe.domain.report.entity.VehicleReport;
import kea.enter.enterbe.domain.report.repository.VehicleReportRepository;
import kea.enter.enterbe.domain.winning.entity.Winning;
import kea.enter.enterbe.domain.winning.entity.WinningState;
import kea.enter.enterbe.domain.winning.repository.WinningRepository;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import kea.enter.enterbe.global.util.ObjectStorageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    public void postTakeVehicleReport(PostTakeVehicleReportServiceDto dto) {
        List<String> images = new ArrayList<>();
        Winning winning = findWinningByMemberIdAndTakeDate(dto.getMemberId(), LocalDate.now(clock));

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
                VehicleReport.takeCreate(winning, frontImg, leftImg,
                    rightImg, backImg, dashboardImg));
            vehicleNoteRepository.save(
                VehicleNote.create(winning.getVehicle(), vehicleReport, dto.getNote()));

        } catch (Exception e) {
            deleteS3Images(images);
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    private Winning findWinningByMemberIdAndTakeDate(Long memberId,LocalDate date) {
        return winningRepository.findByMemberIdAndTakeDateAndState(memberId, date,WinningState.ACTIVE)
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
