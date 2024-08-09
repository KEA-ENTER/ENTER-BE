package kea.enter.enterbe.domain.vehicle.repository;

import kea.enter.enterbe.domain.vehicle.entity.VehicleNote;
import kea.enter.enterbe.domain.vehicle.entity.VehicleNoteState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VehicleNoteRepository extends JpaRepository<VehicleNote, Long> {
    Optional<VehicleNote> findByVehicleIdAndState(Long vehicleId, VehicleNoteState state);
}
