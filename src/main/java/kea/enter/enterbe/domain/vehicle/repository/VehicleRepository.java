package kea.enter.enterbe.domain.vehicle.repository;

import java.util.List;
import java.util.Optional;
import kea.enter.enterbe.api.vehicle.controller.dto.request.VehicleSearchCategory;
import kea.enter.enterbe.api.vehicle.controller.dto.response.GetAdminVehicleResponse;
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
    Optional<Vehicle> findByIdAndStateNot(Long id, VehicleState vehicleState);
    Page<Vehicle> findBySearchOption(Pageable pageable, VehicleSearchCategory searchCategory, String word);
    GetAdminVehicleResponse findVehicleAndNotebyId(Long id);
    List<Vehicle> findByStateIn(List<VehicleState> states);

}