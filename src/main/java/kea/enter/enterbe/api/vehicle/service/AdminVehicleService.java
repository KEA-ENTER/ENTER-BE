package kea.enter.enterbe.api.vehicle.service;

import kea.enter.enterbe.api.vehicle.controller.dto.response.AdminVehicleResponse;
import kea.enter.enterbe.api.vehicle.service.dto.CreateVehicleDto;
import kea.enter.enterbe.api.vehicle.service.dto.ModifyVehicleDto;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminVehicleService {
    Page<AdminVehicleResponse> getVehicleList(Pageable pageable, String vehicleNo, String model, VehicleState state);
    void createVehicle(CreateVehicleDto dto);
    void modifyVehicle(ModifyVehicleDto dto);
}
