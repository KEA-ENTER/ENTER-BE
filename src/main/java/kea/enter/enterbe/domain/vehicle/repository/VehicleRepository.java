package kea.enter.enterbe.domain.vehicle.repository;

import java.util.List;
import java.util.Optional;
import kea.enter.enterbe.domain.report.entity.VehicleReport;
import kea.enter.enterbe.domain.report.entity.VehicleReportState;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByVehicleNo(String vehicleNo);
    Optional<Vehicle> findByVehicleNoAndState(String vehicleNo, VehicleState state);

    Optional<Vehicle> findByVehicleNoAndStateNot(String vehicleNo, VehicleState state);
}
