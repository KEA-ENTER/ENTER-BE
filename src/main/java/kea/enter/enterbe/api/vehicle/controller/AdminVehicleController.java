package kea.enter.enterbe.api.vehicle.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kea.enter.enterbe.api.vehicle.controller.dto.request.GetAdminVehicleListRequest;
import kea.enter.enterbe.api.vehicle.controller.dto.request.PostAdminVehicleRequest;
import kea.enter.enterbe.api.vehicle.controller.dto.response.GetAdminVehicleListResponse;
import kea.enter.enterbe.api.vehicle.controller.dto.response.GetAdminVehicleResponse;
import kea.enter.enterbe.api.vehicle.service.AdminVehicleService;
import kea.enter.enterbe.api.vehicle.service.dto.CreateVehicleServiceDto;
import kea.enter.enterbe.api.vehicle.service.dto.DeleteVehicleServiceDto;
import kea.enter.enterbe.api.vehicle.service.dto.GetVehicleListServiceDto;
import kea.enter.enterbe.api.vehicle.service.dto.ModifyVehicleServiceDto;
import kea.enter.enterbe.global.common.api.CustomResponseCode;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import kea.enter.enterbe.global.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Operation(summary = "법인 차량 목록 조회 API")
    @GetMapping("")
    public GetAdminVehicleListResponse getVehicleList(
        @PageableDefault(size = 4) Pageable pageable,
        @ModelAttribute GetAdminVehicleListRequest request) {

        return adminVehicleService.getVehicleList(
            GetVehicleListServiceDto.of(
                request.getWord(),
                request.getSearchCategory(),
                pageable));
    }

    @Operation(summary = "법인 차량 상세 정보 조회 API")
    @GetMapping("/{vehicleId}")
    public GetAdminVehicleResponse getVehicle(
        @PathVariable Long vehicleId) {

        return adminVehicleService.getVehicle(vehicleId);
    }

    @Operation(summary = "법인 차량 등록 API")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CustomResponseCode> createVehicle(
        @RequestPart(value = "data") @Valid PostAdminVehicleRequest postAdminVehicleRequest,
        @RequestPart(value = "image") MultipartFile img) {

        // 이미지 파일 확인
        if (!fileUtil.isImageFile(img))
            throw new CustomException(ResponseCode.NOT_IMAGE_FILE);

        adminVehicleService.createVehicle(
            CreateVehicleServiceDto.of(postAdminVehicleRequest.getVehicleNo(), postAdminVehicleRequest.getCompany(),
                postAdminVehicleRequest.getModel(), postAdminVehicleRequest.getSeats(),
                postAdminVehicleRequest.getFuel(), img, postAdminVehicleRequest.getState()));

        return ResponseEntity.ok(CustomResponseCode.SUCCESS);
    }

    @Operation(summary = "법인 차량 수정 API")
    @PatchMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CustomResponseCode> modifyVehicle(
        @RequestPart(value = "data") @Valid PostAdminVehicleRequest postAdminVehicleRequest,
        @RequestPart(value = "image") MultipartFile img) {

        // 이미지 파일 확인
        if (!fileUtil.isImageFile(img))
            throw new CustomException(ResponseCode.NOT_IMAGE_FILE);

        adminVehicleService.modifyVehicle(
            ModifyVehicleServiceDto.of(postAdminVehicleRequest.getId(), postAdminVehicleRequest.getVehicleNo(), postAdminVehicleRequest.getCompany(),
                postAdminVehicleRequest.getModel(), postAdminVehicleRequest.getSeats(),
                postAdminVehicleRequest.getFuel(), img, postAdminVehicleRequest.getState()));

        return ResponseEntity.ok(CustomResponseCode.SUCCESS);
    }

    @Operation(summary = "법인 차량 삭제 API")
    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<CustomResponseCode> deleteVehicle(
        @PathVariable Long vehicleId) {

        adminVehicleService.deleteVehicle(DeleteVehicleServiceDto.of(vehicleId));

        return ResponseEntity.ok(CustomResponseCode.SUCCESS);
    }
}