package kea.enter.enterbe.api.controller.vehicle;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kea.enter.enterbe.api.controller.vehicle.dto.request.VehicleRequest;
import kea.enter.enterbe.api.controller.vehicle.dto.response.VehicleResponse;
import kea.enter.enterbe.api.service.vehicle.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "법인 차량 관리", description = "법인 차량 관리 API 명세서")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/vehicles")
public class VehicleController {
    private final VehicleService vehicleService;

    @Operation(summary = "법인 차량 등록 API")
    @PostMapping()
    public ResponseEntity<Long> createVehicle(@Valid @RequestBody VehicleRequest vehicleRequest) {
        return ResponseEntity.ok(vehicleService.createVehicle(vehicleRequest.toService()));
    }

    @Operation(summary = "법인 차량 수정 API")
    @PatchMapping("/{vehicleId}")
    public ResponseEntity<VehicleResponse> modifyVehicle(@Valid @RequestBody VehicleRequest vehicleRequest) {
        return ResponseEntity.ok(vehicleService.modifyVehicle(vehicleRequest.toService()));
    }

    @Operation(summary = "법인 차량 삭제 API")
    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<VehicleResponse> deleteVehicle(@Valid @RequestBody VehicleRequest vehicleRequest) {
        return ResponseEntity.ok(vehicleService.deleteVehicle(vehicleRequest.toService()));
    }

    @Operation(summary = "법인 차량 목록 조회 API")
    @GetMapping()
    public ResponseEntity<VehicleResponse> getVehicleList(@Valid @RequestBody VehicleRequest vehicleRequest) {
        return ResponseEntity.ok(vehicleService.getVehicleList(vehicleRequest.toService()));
    }

    @Operation(summary = "법인 차량 상세 정보 조회 API")
    @GetMapping("/{vehicleId}")
    public ResponseEntity<VehicleResponse> getVehicle(@Valid @RequestBody VehicleRequest vehicleRequest) {
        return ResponseEntity.ok(vehicleService.getVehicle(vehicleRequest.toService()));
    }
}
