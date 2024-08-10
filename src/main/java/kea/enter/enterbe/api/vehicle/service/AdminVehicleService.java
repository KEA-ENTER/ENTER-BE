package kea.enter.enterbe.api.vehicle.service;

import kea.enter.enterbe.api.vehicle.controller.dto.response.AdminVehicleListResponse;
import kea.enter.enterbe.api.vehicle.controller.dto.response.AdminVehicleResponse;
import kea.enter.enterbe.api.vehicle.service.dto.CreateVehicleDto;
import kea.enter.enterbe.api.vehicle.service.dto.DeleteVehicleDto;
import kea.enter.enterbe.api.vehicle.service.dto.ModifyVehicleDto;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminVehicleService {
    Page<AdminVehicleListResponse> getVehicleList(Pageable pageable, String vehicleNo, String model, VehicleState state);
    AdminVehicleResponse getVehicle(Long id);
    void createVehicle(CreateVehicleDto dto);
    void modifyVehicle(ModifyVehicleDto dto);
    void deleteVehicle(DeleteVehicleDto dto);
}
