package kea.enter.enterbe.api.vehicle.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import kea.enter.enterbe.IntegrationTestSupport;
import kea.enter.enterbe.api.vehicle.service.dto.CreateVehicleDto;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
import kea.enter.enterbe.domain.vehicle.entity.VehicleFuel;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import kea.enter.enterbe.global.util.ObjectStorageUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
class AdminVehicleServiceImplTest extends IntegrationTestSupport {

    @DisplayName(value = "법인 차량 추가 Serv (성공)")
    @Test
    public void createVehicleSuccess() {
        // given
        CreateVehicleDto dto = CreateVehicleDto.of("12가3456", "현대", "그랜저",
            5, VehicleFuel.GASOLINE, mock(MultipartFile.class), VehicleState.AVAILABLE);
            given(objectStorageUtil.uploadFileToS3(mock(MultipartFile.class))).willReturn("test.jpg");
//        // 중복 아님 확인
//        given(vehicleRepository.findByVehicleNoAndStateNot(dto.getVehicleNo(), VehicleState.INACTIVE))
//            .willReturn(null);
//
//        given(vehicleRepository.save(any(Vehicle.class)))
//            .willReturn(vehicle);

        // when
        adminVehicleService.createVehicle(dto);

        // then
        Vehicle vehicle = vehicleRepository.findByVehicleNoAndState(dto.getVehicleNo(), dto.getState());
//        given(vehicleRepository.findByVehicleNoAndState(dto.getVehicleNo(), dto.getState()))
//            .willReturn(vehicle);

//        Vehicle findVehicle = vehicleRepository.findByVehicleNoAndState(dto.getVehicleNo(), dto.getState());

        assertThat(vehicle.getVehicleNo()).isEqualTo(dto.getVehicleNo());
        assertThat(vehicle.getState()).isEqualTo(dto.getState());
    }

    @DisplayName(value = "법인 차량 추가 Serv (차량번호 형식 오류)")
    @Test
    public void createVehicleWithInvalidVehicleNo() {
        // given
        CreateVehicleDto dto = CreateVehicleDto.of("12가34567", "현대", "그랜저",
            5, VehicleFuel.GASOLINE, mock(MultipartFile.class), VehicleState.AVAILABLE);

        // when & then
        assertThatThrownBy(() -> adminVehicleService.createVehicle(dto))
            .isInstanceOf(CustomException.class)
            .extracting("responseCode")
            .isEqualTo(ResponseCode.VEHICLE_NO_NOT_ALLOWED);
    }
}