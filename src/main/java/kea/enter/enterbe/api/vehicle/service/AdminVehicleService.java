package kea.enter.enterbe.api.vehicle.service;

import kea.enter.enterbe.api.vehicle.service.dto.CreateVehicleDto;
import kea.enter.enterbe.api.vehicle.service.dto.ModifyVehicleDto;

public interface AdminVehicleService {
    void createVehicle(CreateVehicleDto dto);
    void modifyVehicle(ModifyVehicleDto dto);
}
