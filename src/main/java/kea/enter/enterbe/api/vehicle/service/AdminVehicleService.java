package kea.enter.enterbe.api.vehicle.service;

import kea.enter.enterbe.api.vehicle.controller.dto.response.AdminVehicleResponse;
import kea.enter.enterbe.api.vehicle.service.dto.CreateVehicleDto;

public interface AdminVehicleService {
    void createVehicle(CreateVehicleDto service);
    AdminVehicleResponse modifyVehicle(CreateVehicleDto service);
    AdminVehicleResponse deleteVehicle(CreateVehicleDto service);
    AdminVehicleResponse getVehicleList(CreateVehicleDto service);
    AdminVehicleResponse getVehicle(CreateVehicleDto service);
}
