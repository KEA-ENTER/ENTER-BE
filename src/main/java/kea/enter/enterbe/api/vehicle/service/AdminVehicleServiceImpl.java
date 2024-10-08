package kea.enter.enterbe.api.vehicle.service;

import java.util.Optional;
import java.util.regex.Pattern;
import kea.enter.enterbe.api.vehicle.controller.dto.request.VehicleSearchCategory;
import kea.enter.enterbe.api.vehicle.controller.dto.response.GetAdminVehicleListResponse;
import kea.enter.enterbe.api.vehicle.controller.dto.response.GetAdminVehicleResponse;
import kea.enter.enterbe.api.vehicle.service.dto.CreateVehicleServiceDto;
import kea.enter.enterbe.api.vehicle.service.dto.DeleteVehicleServiceDto;
import kea.enter.enterbe.api.vehicle.service.dto.GetVehicleListServiceDto;
import kea.enter.enterbe.api.vehicle.service.dto.ModifyVehicleServiceDto;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import kea.enter.enterbe.domain.vehicle.repository.VehicleRepository;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import kea.enter.enterbe.global.util.ObjectStorageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminVehicleServiceImpl implements AdminVehicleService {
    private final VehicleRepository vehicleRepository;
    private final ObjectStorageUtil objectStorageUtil;

    @Override
    public GetAdminVehicleListResponse getVehicleList(GetVehicleListServiceDto dto) {
        VehicleSearchCategory searchCategory;
        try {
            searchCategory = VehicleSearchCategory.valueOf(dto.getSearchCategory().toUpperCase());

        } catch (IllegalArgumentException e) {
            throw new CustomException(ResponseCode.VEHICLE_SEARCH_CATEGORY_NOT_FOUND);
        }

        Page<Vehicle> vehicles = vehicleRepository.findBySearchOption(
            dto.getPageable(), searchCategory, dto.getWord());

        return GetAdminVehicleListResponse.of(
            vehicles.getContent(),
            vehicles.getNumber(),
            vehicles.getSize(),
            vehicles.getTotalElements(),
            vehicles.getTotalPages(),
            vehicles.hasNext()
        );
    }

    @Override
    public GetAdminVehicleResponse getVehicle(Long id) {
        if (vehicleRepository.findByIdAndStateNot(id, VehicleState.INACTIVE).isPresent())
            return vehicleRepository.findVehicleAndNotebyId(id);

        else
            throw new CustomException(ResponseCode.VEHICLE_NOT_VALID);
    }

    @Override
    @Transactional
    public void createVehicle(CreateVehicleServiceDto dto) {
        checkVehicle(dto.getVehicleNo());

        String img = "";
        try {
            img = uploadS3Image(dto.getImg());

            vehicleRepository.save(Vehicle.of(
                dto.getVehicleNo(), dto.getCompany(), dto.getModel(), dto.getSeats(),
                dto.getFuel(), img, dto.getState()));

        } catch (Exception e) {
            deleteS3Image(img);
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public void modifyVehicle(ModifyVehicleServiceDto dto) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(dto.getId());

        if (vehicle.isPresent()) {
            if (!vehicle.get().getVehicleNo().equals(dto.getVehicleNo()))
                checkVehicle(dto.getVehicleNo());

            String img = "";
            try {
                img = uploadS3Image(dto.getImg());

                vehicle.get().patchVehicle(
                    dto.getVehicleNo(), dto.getCompany(), dto.getModel(), dto.getSeats(),
                    dto.getFuel(), img, dto.getState());

            } catch (Exception e) {
                deleteS3Image(img);
                throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
            }
        }
        else {
            throw new CustomException(ResponseCode.VEHICLE_NOT_VALID);
        }
    }

    @Override
    @Transactional
    public void deleteVehicle(DeleteVehicleServiceDto dto) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(dto.getId());
        if (vehicle.isPresent()) {
            vehicle.get().deleteVehicle();
        }
        else {
            throw new CustomException(ResponseCode.VEHICLE_NOT_VALID);
        }
    }


    private void checkVehicle(String vehicleNo) {
        // 차량 번호 형식 : 두 자리 또는 세 자리 숫자 + 한글 한 글자 + 네 자리 숫자
        String VEHICLE_NO_PATTERN = "^[0-9]{2,3}[가-힣][0-9]{4}$";

        // 유효성 검사
        if (!Pattern.matches(VEHICLE_NO_PATTERN, vehicleNo)) {
            throw new CustomException(ResponseCode.VEHICLE_NO_NOT_ALLOWED);
        }

        // 중복 확인
        if (vehicleRepository.findByVehicleNoAndStateNot(vehicleNo, VehicleState.INACTIVE).isPresent()) {
            throw new CustomException(ResponseCode.VEHICLE_DUPLICATED);
        }
    }

    private void deleteS3Image(String imageUrl) {
        objectStorageUtil.delete(imageUrl);
    }

    private String uploadS3Image(MultipartFile images) {
        return objectStorageUtil.uploadFileToS3(images);
    }

}