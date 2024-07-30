package kea.enter.enterbe.api.vehicle.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kea.enter.enterbe.api.vehicle.service.VehicleService;
import kea.enter.enterbe.api.vehicle.service.dto.PostTakeVehicleReportServiceDto;
import kea.enter.enterbe.global.common.api.CustomResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "예제", description = "예제 API 명세서")
@RestController
@RequiredArgsConstructor
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    @Operation(summary = "인수보고서 작성")
    @PostMapping(value = "/reports/take",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<CustomResponseCode> postTakeVehicleReport(
        @RequestPart(value = "front_img") MultipartFile front_img,
        @RequestPart(value = "right_img") MultipartFile right_img,
        @RequestPart(value = "back_img") MultipartFile back_img,
        @RequestPart(value = "left_img") MultipartFile left_img,
        @RequestPart(value = "dashboard_img") MultipartFile dashboardImg,
        @RequestPart(value = "note") String note) {
        //todo: spring security 구현 완료 시 token에서 memberId 값 가져오기
        Long memberId = 1L;
        vehicleService.postTakeVehicleReport(
            PostTakeVehicleReportServiceDto.of(memberId, front_img, right_img, back_img, left_img,
                dashboardImg, note));
        return ResponseEntity.ok(CustomResponseCode.SUCCESS);
    }
}
