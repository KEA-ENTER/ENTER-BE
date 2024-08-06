package kea.enter.enterbe.api.vehicle.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import kea.enter.enterbe.IntegrationTestSupport;
import kea.enter.enterbe.api.vehicle.service.dto.CreateVehicleDto;
import kea.enter.enterbe.api.vehicle.service.dto.ModifyVehicleDto;
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

        // when
        adminVehicleService.createVehicle(dto);

        // then
        Vehicle vehicle = vehicleRepository.findByVehicleNoAndState(dto.getVehicleNo(), dto.getState());

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


    @DisplayName(value = "법인 차량 수정 Serv (성공)")
    @Test
    public void modifyVehicleSuccess() {
        // given
        Vehicle vehicle = new Vehicle("12가34567", "현대", "그랜저", 5,
            VehicleFuel.GASOLINE, "oldImage.jpg", VehicleState.AVAILABLE);
        vehicleRepository.save(vehicle);

        MultipartFile image = mock(MultipartFile.class);

        ModifyVehicleDto dto = ModifyVehicleDto.of(vehicle.getId(), "12가3455",
            "현대", "그랜저", 4, VehicleFuel.GASOLINE, image,
            VehicleState.AVAILABLE);

        given(objectStorageUtil.uploadFileToS3(image)).willReturn("test.jpg");

        // when
        adminVehicleService.modifyVehicle(dto);

        // then
        vehicle = vehicleRepository.findById(dto.getId()).get();

        assertThat(vehicle.getVehicleNo()).isEqualTo(dto.getVehicleNo());
        assertThat(vehicle.getCompany()).isEqualTo(dto.getCompany());
        assertThat(vehicle.getModel()).isEqualTo(dto.getModel());
        assertThat(vehicle.getSeats()).isEqualTo(dto.getSeats());
        assertThat(vehicle.getImg()).isEqualTo("test.jpg");
        assertThat(vehicle.getFuel()).isEqualTo(dto.getFuel());
        assertThat(vehicle.getState()).isEqualTo(dto.getState());
    }

    @DisplayName(value = "법인 차량 수정 Serv (차량번호 형식 오류)")
    @Test
    public void modifyVehicleSuccessWithInvalidVehicleNo() {
        // given
        Vehicle vehicle = new Vehicle("12가3456", "현대", "그랜저", 5,
            VehicleFuel.GASOLINE, "oldImage.jpg", VehicleState.AVAILABLE);
        vehicleRepository.save(vehicle);

        MultipartFile image = mock(MultipartFile.class);

        ModifyVehicleDto dto = ModifyVehicleDto.of(vehicle.getId(), "12가34567",
            "현대", "그랜저", 4, VehicleFuel.GASOLINE, image,
            VehicleState.AVAILABLE);

        given(objectStorageUtil.uploadFileToS3(image)).willReturn("test.jpg");

        // when & then
        assertThatThrownBy(() -> adminVehicleService.modifyVehicle(dto))
            .isInstanceOf(CustomException.class)
            .extracting("responseCode")
            .isEqualTo(ResponseCode.VEHICLE_NO_NOT_ALLOWED);
    }
}