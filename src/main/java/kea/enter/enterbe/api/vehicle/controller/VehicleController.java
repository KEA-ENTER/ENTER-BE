package kea.enter.enterbe.api.vehicle.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import kea.enter.enterbe.api.vehicle.service.VehicleService;
import kea.enter.enterbe.api.vehicle.service.dto.PostVehicleReportServiceDto;
import kea.enter.enterbe.domain.take.entity.VehicleReportType;
import kea.enter.enterbe.global.common.api.CustomResponseCode;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import kea.enter.enterbe.global.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "[사용자] 차량 관련 API", description = "Vehicle")
@RestController
@RequiredArgsConstructor
@RequestMapping("/vehicles")
@Validated
public class VehicleController {

    private final VehicleService vehicleService;
    private final FileUtil fileUtil;

    @Operation(summary = "보고서 작성")
    @PostMapping(value = "/reports", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
        MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<CustomResponseCode> postTakeVehicleReport(
        @RequestPart(value = "front_img") MultipartFile front_img,
        @RequestPart(value = "right_img") MultipartFile right_img,
        @RequestPart(value = "back_img") MultipartFile back_img,
        @RequestPart(value = "left_img") MultipartFile left_img,
        @RequestPart(value = "dashboard_img") MultipartFile dashboardImg,
        @RequestPart(value = "note",required = false) String note,
        @RequestPart(value = "parking_loc",required = false) String parkingLoc,
        @RequestParam(name = "type") @NotNull(message = "보고서 종류를 입력해주세요")
        @Schema(description = "보고서 종류 TAKE, RETURN", example = "TAKE")
        VehicleReportType type
    ) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = Long.valueOf(loggedInUser.getName());

        if (!fileUtil.isImageFileList(
            List.of(front_img, right_img, back_img, left_img, dashboardImg))) {
            throw new CustomException(ResponseCode.NOT_IMAGE_FILE);
        }
        if(type.equals(VehicleReportType.RETURN) && !StringUtils.hasText(parkingLoc)){
            throw new CustomException(ResponseCode.NEED_PARKING_LOC_FOR_RETURN_REPORT);
        }
        vehicleService.postVehicleReport(
            PostVehicleReportServiceDto.of(memberId, front_img, right_img, back_img, left_img,
                dashboardImg, note, parkingLoc, type));
        return ResponseEntity.ok(CustomResponseCode.SUCCESS);
    }
}
