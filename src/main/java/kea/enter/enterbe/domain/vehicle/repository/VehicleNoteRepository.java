package kea.enter.enterbe.domain.vehicle.repository;

import kea.enter.enterbe.domain.vehicle.entity.VehicleNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleNoteRepository extends JpaRepository<VehicleNote, Long> {

}
