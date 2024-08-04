package kea.enter.enterbe.domain.vehicle.repository;

import java.util.List;
import java.util.Optional;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long>, VehicleRepositoryCustom {
    Optional<Vehicle> findById(Long id);
    Vehicle findByVehicleNoAndState(String vehicleNo, VehicleState state);
    Optional<Vehicle> findByVehicleNoAndStateNot(String vehicleNo, VehicleState state);

    List<Vehicle> searchVehicle(String vehicleNo, String model, VehicleState state);

    List<Vehicle> findAllByModel(String model);
    List<Vehicle> findAllByVehicleNo(String vehicleNo);
    List<Vehicle> findAllByState(VehicleState state);
}

