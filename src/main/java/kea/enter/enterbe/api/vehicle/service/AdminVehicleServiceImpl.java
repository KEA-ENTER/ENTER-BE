package kea.enter.enterbe.api.vehicle.service;

import java.time.Clock;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import kea.enter.enterbe.api.vehicle.controller.dto.response.AdminVehicleResponse;
import kea.enter.enterbe.api.vehicle.service.dto.CreateVehicleDto;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import kea.enter.enterbe.domain.vehicle.repository.VehicleRepository;
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
public class AdminVehicleServiceImpl implements AdminVehicleService {
    private final VehicleRepository vehicleRepository;
    private final ObjectStorageUtil objectStorageUtil;

    public Optional<Vehicle> checkVehicle(String vehicleNo) {
        // 차량 번호 형식 : 두 자리 또는 세 자리 숫자 + 한글 한 글자 + 네 자리 숫자
        String VEHICLE_NO_PATTERN = "^[0-9]{2,3}[가-힣][0-9]{4}$";

        // 유효성 검사
        if (!Pattern.matches(VEHICLE_NO_PATTERN, vehicleNo)) {
            throw new CustomException(ResponseCode.VEHICLE_NO_NOT_ALLOWED);
        }

        // 중복 확인
        Optional<Vehicle> vehicle = vehicleRepository.findByVehicleNoAndStateNot(vehicleNo, VehicleState.INACTIVE);

        return vehicle;
    }

    @Override
    @Transactional
    public void createVehicle(CreateVehicleDto dto) {
        Optional<Vehicle> vehicle = checkVehicle(dto.getVehicleNo());

        if (vehicle.isPresent()) {
            throw new CustomException(ResponseCode.VEHICLE_DUPLICATED);
        }
        else {
            String img = "";
            img = uploadS3Image(dto.getImg());

            try {
                img = uploadS3Image(dto.getImg());

                vehicle = Optional.of(vehicleRepository.save(Vehicle.of(
                    dto.getVehicleNo(), dto.getCompany(), dto.getModel(), dto.getSeats(),
                    dto.getFuel(), img, VehicleState.AVAILABLE)));

            } catch (Exception e) {
                deleteS3Image(img);
                throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Override
    public AdminVehicleResponse modifyVehicle(CreateVehicleDto service) {
        return null;
    }

    @Override
    public AdminVehicleResponse deleteVehicle(CreateVehicleDto service) {
        return null;
    }

    @Override
    public AdminVehicleResponse getVehicleList(CreateVehicleDto service) {
        return null;
    }

    @Override
    public AdminVehicleResponse getVehicle(CreateVehicleDto service) {
        return null;
    }

    private void deleteS3Image(String imageUrl) {
        objectStorageUtil.delete(imageUrl);
    }

    private String uploadS3Image(MultipartFile images) {
        return objectStorageUtil.uploadFileToS3(images);
    }
}