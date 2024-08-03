package kea.enter.enterbe.api.vehicle.service;

import java.util.Optional;
import kea.enter.enterbe.api.vehicle.service.dto.CreateVehicleDto;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;

public interface AdminVehicleService {
    void createVehicle(CreateVehicleDto dto);
}
