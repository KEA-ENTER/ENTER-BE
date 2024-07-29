package kea.enter.enterbe.domain.note.repository;

import kea.enter.enterbe.domain.note.entity.VehicleNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleNoteRepository extends JpaRepository<VehicleNote, Long> {

}
