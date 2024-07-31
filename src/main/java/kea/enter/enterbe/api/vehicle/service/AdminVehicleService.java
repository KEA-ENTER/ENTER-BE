package kea.enter.enterbe.api.vehicle.service;

import kea.enter.enterbe.api.vehicle.controller.dto.response.AdminVehicleResponse;
import kea.enter.enterbe.api.vehicle.service.dto.AdminVehicleDto;

public interface AdminVehicleService {
    void createVehicle(AdminVehicleDto service);
    AdminVehicleResponse modifyVehicle(AdminVehicleDto service);
    AdminVehicleResponse deleteVehicle(AdminVehicleDto service);
    AdminVehicleResponse getVehicleList(AdminVehicleDto service);
    AdminVehicleResponse getVehicle(AdminVehicleDto service);
}
