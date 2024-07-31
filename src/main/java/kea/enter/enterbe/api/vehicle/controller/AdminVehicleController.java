package kea.enter.enterbe.api.vehicle.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kea.enter.enterbe.api.vehicle.controller.dto.request.AdminVehicleRequest;
import kea.enter.enterbe.api.vehicle.controller.dto.response.AdminVehicleResponse;
import kea.enter.enterbe.api.vehicle.service.AdminVehicleService;
import kea.enter.enterbe.api.vehicle.service.dto.CreateVehicleDto;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import kea.enter.enterbe.global.common.api.CustomResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @Operation(summary = "법인 차량 등록 API")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CustomResponseCode> createVehicle(
        @RequestPart(value = "data") @Valid AdminVehicleRequest adminVehicleRequest,
        @RequestPart(value = "image") MultipartFile img) {
        adminVehicleService.createVehicle(
            CreateVehicleDto.of(adminVehicleRequest.getVehicleNo(), adminVehicleRequest.getCompany(),
                adminVehicleRequest.getModel(),adminVehicleRequest.getSeats(),
                adminVehicleRequest.getFuel(), img, VehicleState.AVAILABLE));

        return ResponseEntity.ok(CustomResponseCode.SUCCESS);
    }

    @Operation(summary = "법인 차량 수정 API")
    @PatchMapping("/{vehicleId}")
    public ResponseEntity<AdminVehicleResponse> modifyVehicle(@Valid @RequestBody AdminVehicleRequest adminVehicleRequest) {
        return ResponseEntity.ok(adminVehicleService.deleteVehicle(adminVehicleRequest.toService()));
    }

    @Operation(summary = "법인 차량 삭제 API")
    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<AdminVehicleResponse> deleteVehicle(@Valid @RequestBody AdminVehicleRequest adminVehicleRequest) {
        return ResponseEntity.ok(adminVehicleService.deleteVehicle(adminVehicleRequest.toService()));
    }

    @Operation(summary = "법인 차량 목록 조회 API")
    @GetMapping()
    public ResponseEntity<AdminVehicleResponse> getVehicleList(@Valid @RequestBody AdminVehicleRequest adminVehicleRequest) {
        return ResponseEntity.ok(adminVehicleService.getVehicleList(adminVehicleRequest.toService()));
    }

    @Operation(summary = "법인 차량 상세 정보 조회 API")
    @GetMapping("/{vehicleId}")
    public ResponseEntity<AdminVehicleResponse> getVehicle(@Valid @RequestBody AdminVehicleRequest adminVehicleRequest) {
        return ResponseEntity.ok(adminVehicleService.getVehicle(adminVehicleRequest.toService()));
    }
}
