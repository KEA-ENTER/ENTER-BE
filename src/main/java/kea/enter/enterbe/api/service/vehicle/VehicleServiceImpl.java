package kea.enter.enterbe.api.service.vehicle;

import java.util.Optional;
import java.util.regex.Pattern;
import kea.enter.enterbe.api.controller.vehicle.dto.response.VehicleResponse;
import kea.enter.enterbe.api.service.vehicle.dto.VehicleDto;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
import kea.enter.enterbe.domain.vehicle.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VehicleServiceImpl implements VehicleService{
    private final VehicleRepository vehicleRepository;

    // 차량 번호 형식 : 두 자리 또는 세 자리 숫자 + 한글 한 글자 + 네 자리 숫자
    private static final String VEHICLE_NO_PATTERN = "^[0-9]{2,3}[가-힣][0-9]{4}$";

    @Override
    @Transactional
    public Long createVehicle(VehicleDto dto) {
        // 유효성 검사
        if(!Pattern.matches(VEHICLE_NO_PATTERN, dto.getVehicleNo())) {
            // 오류 메시지

            return 0l;
        }

        // 중복 확인
        Optional<Vehicle> vehicle = vehicleRepository.findByVehicleNo(dto.getVehicleNo());
        if(vehicle.isEmpty()) {
            vehicle = Optional.of(vehicleRepository.save(Vehicle.of(
                dto.getVehicleNo(), dto.getCompany(), dto.getModel(), dto.getSeats(), dto.getFuel(), dto.getImg(), dto.getState())));

            return vehicle.get().getId();
        }

        return 0l;
    }

    @Override
    @Transactional
    public VehicleResponse modifyVehicle(VehicleDto service) {
        return null;
    }

    @Override
    @Transactional
    public VehicleResponse deleteVehicle(VehicleDto service) {
        return null;
    }

    @Override
    public VehicleResponse getVehicleList(VehicleDto service) {
        return null;
    }

    @Override
    public VehicleResponse getVehicle(VehicleDto service) {
        return null;
    }
}