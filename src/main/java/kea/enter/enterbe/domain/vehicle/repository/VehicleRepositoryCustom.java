package kea.enter.enterbe.domain.vehicle.repository;

import kea.enter.enterbe.api.vehicle.controller.dto.request.VehicleSearchCategory;
import kea.enter.enterbe.api.vehicle.controller.dto.response.GetAdminVehicleResponse;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VehicleRepositoryCustom {
    Page<Vehicle> findBySearchOption(Pageable pageable, VehicleSearchCategory searchCategory, String word);
    GetAdminVehicleResponse findVehicleAndNotebyId(Long id);
}
