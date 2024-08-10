package kea.enter.enterbe.domain.vehicle.repository;

import kea.enter.enterbe.api.vehicle.controller.dto.response.AdminVehicleResponse;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VehicleRepositoryCustom {
    Page<Vehicle> findBySearchOption(Pageable pageable, String vehicleNo, String model, VehicleState state);
    AdminVehicleResponse findAdminVehicleResponsebyId(Long id);
}
