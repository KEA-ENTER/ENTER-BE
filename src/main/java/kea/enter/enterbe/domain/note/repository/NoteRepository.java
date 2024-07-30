package kea.enter.enterbe.domain.note.repository;

import java.util.Optional;
import kea.enter.enterbe.domain.note.entity.VehicleNote;
import kea.enter.enterbe.domain.note.entity.VehicleNoteState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<VehicleNote, Long> {
    Optional<VehicleNote> findByVehicleId(long vehicleId);
    Optional<VehicleNote> findByVehicleNoteState(VehicleNoteState state);
}