package kea.enter.enterbe.domain.vehicle.repository;

import java.util.List;
import java.util.Optional;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findAllByModel(String model);
    Optional<Vehicle> findByVehicleNo(String vehicleNo);
    List<Vehicle> findAllByState(VehicleState state);
}
