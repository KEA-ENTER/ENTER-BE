package kea.enter.enterbe.domain.vehicle.repository;

import java.util.Optional;
import kea.enter.enterbe.api.vehicle.controller.dto.response.AdminVehicleResponse;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long>, VehicleRepositoryCustom {
    Optional<Vehicle> findById(Long id);
    Vehicle findByVehicleNoAndState(String vehicleNo, VehicleState state);
    Optional<Vehicle> findByVehicleNoAndStateNot(String vehicleNo, VehicleState state);

    Page<Vehicle> findBySearchOption(Pageable pageable, String vehicleNo, String model, VehicleState state);
    AdminVehicleResponse findAdminVehicleResponsebyId(Long id);

}