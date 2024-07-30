package kea.enter.enterbe.api.service.vehicle;

import kea.enter.enterbe.api.controller.vehicle.dto.response.VehicleResponse;
import kea.enter.enterbe.api.service.vehicle.dto.VehicleDto;

public interface VehicleService {
    Long createVehicle(VehicleDto service);
    VehicleResponse modifyVehicle(VehicleDto service);
    VehicleResponse deleteVehicle(VehicleDto service);
    VehicleResponse getVehicleList(VehicleDto service);
    VehicleResponse getVehicle(VehicleDto service);
}
