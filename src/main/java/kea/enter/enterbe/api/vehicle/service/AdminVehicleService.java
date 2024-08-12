package kea.enter.enterbe.api.vehicle.service;

import kea.enter.enterbe.api.vehicle.controller.dto.response.GetAdminVehicleListResponse;
import kea.enter.enterbe.api.vehicle.controller.dto.response.GetAdminVehicleResponse;
import kea.enter.enterbe.api.vehicle.service.dto.CreateVehicleServiceDto;
import kea.enter.enterbe.api.vehicle.service.dto.DeleteVehicleServiceDto;
import kea.enter.enterbe.api.vehicle.service.dto.GetVehicleListServiceDto;
import kea.enter.enterbe.api.vehicle.service.dto.ModifyVehicleServiceDto;

public interface AdminVehicleService {
    GetAdminVehicleListResponse getVehicleList(GetVehicleListServiceDto dto);
    GetAdminVehicleResponse getVehicle(Long id);
    void createVehicle(CreateVehicleServiceDto dto);
    void modifyVehicle(ModifyVehicleServiceDto dto);
    void deleteVehicle(DeleteVehicleServiceDto dto);
}
