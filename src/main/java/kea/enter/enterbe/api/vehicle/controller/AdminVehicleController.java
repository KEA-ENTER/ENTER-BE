package kea.enter.enterbe.api.vehicle.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kea.enter.enterbe.api.vehicle.controller.dto.request.AdminVehicleRequest;
import kea.enter.enterbe.api.vehicle.service.AdminVehicleService;
import kea.enter.enterbe.api.vehicle.service.dto.CreateVehicleDto;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import kea.enter.enterbe.global.common.api.CustomResponseCode;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import kea.enter.enterbe.global.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "법인 차량 관리", description = "법인 차량 관리 API 명세서")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/vehicles")
public class AdminVehicleController {
    private final AdminVehicleService adminVehicleService;
    private final FileUtil fileUtil;

    @Operation(summary = "법인 차량 등록 API")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CustomResponseCode> createVehicle(
        @RequestPart(value = "data") @Valid AdminVehicleRequest adminVehicleRequest,
        @RequestPart(value = "image") MultipartFile img) {

        // 이미지 파일 확인
        if (!fileUtil.isImageFile(img))
            throw new CustomException(ResponseCode.NOT_IMAGE_FILE);

        adminVehicleService.createVehicle(
            CreateVehicleDto.of(adminVehicleRequest.getVehicleNo(), adminVehicleRequest.getCompany(),
                adminVehicleRequest.getModel(),adminVehicleRequest.getSeats(),
                adminVehicleRequest.getFuel(), img, VehicleState.AVAILABLE));

        return ResponseEntity.ok(CustomResponseCode.SUCCESS);
    }
}
