package kea.enter.enterbe.api.vehicle.service;

import java.util.Optional;
import java.util.regex.Pattern;
import kea.enter.enterbe.api.vehicle.controller.dto.response.AdminVehicleResponse;
import kea.enter.enterbe.api.vehicle.service.AdminVehicleService;
import kea.enter.enterbe.api.vehicle.service.dto.AdminVehicleDto;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
import kea.enter.enterbe.domain.vehicle.repository.VehicleRepository;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminVehicleServiceImpl implements AdminVehicleService {
    private final VehicleRepository vehicleRepository;

    // 차량 번호 형식 : 두 자리 또는 세 자리 숫자 + 한글 한 글자 + 네 자리 숫자
    private static final String VEHICLE_NO_PATTERN = "^[0-9]{2,3}[가-힣][0-9]{4}$";

    public Optional<Vehicle> checkVehicle(String vehicleNo) {
        // 유효성 검사
        if (!Pattern.matches(VEHICLE_NO_PATTERN, vehicleNo)) {
            throw new CustomException(ResponseCode.VEHICLE_NO_NOT_ALLOWED);
        }

        // 중복 확인
        Optional<Vehicle> vehicle = vehicleRepository.findByVehicleNo(vehicleNo);
        if (vehicle.isEmpty()) {
            return vehicle;
        }

        vehicle = vehicleRepository.findByVehicleNo(vehicleNo);

        return vehicle;
    }

    @Override
    @Transactional
    public void createVehicle(AdminVehicleDto dto) {
        Optional<Vehicle> vehicle = checkVehicle(dto.getVehicleNo());

        if (vehicle.isEmpty()) {
            vehicle = Optional.of(vehicleRepository.save(Vehicle.of(
                dto.getVehicleNo(), dto.getCompany(), dto.getModel(), dto.getSeats(), dto.getFuel(), dto.getImg(), dto.getState())));
        }
    }

    @Override
    @Transactional
    public AdminVehicleResponse modifyVehicle(AdminVehicleDto dto) {
        return null;
    }

    @Override
    @Transactional
    public AdminVehicleResponse deleteVehicle(AdminVehicleDto service) {
        return null;
    }

    @Override
    public AdminVehicleResponse getVehicleList(AdminVehicleDto service) {
        return null;
    }

    @Override
    public AdminVehicleResponse getVehicle(AdminVehicleDto service) {
        return null;
    }
}