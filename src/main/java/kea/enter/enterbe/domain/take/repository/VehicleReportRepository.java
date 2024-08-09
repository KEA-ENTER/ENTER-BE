package kea.enter.enterbe.domain.take.repository;

import kea.enter.enterbe.domain.take.entity.VehicleReport;
import kea.enter.enterbe.domain.take.entity.VehicleReportState;
import kea.enter.enterbe.domain.take.entity.VehicleReportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleReportRepository extends JpaRepository<VehicleReport,Long> {

    VehicleReport findByWinningIdAndState(Long id, VehicleReportState vehicleReportState);

    Optional<VehicleReport> findByWinningIdAndTypeAndState(Long id, VehicleReportType type, VehicleReportState vehicleReportState);

    List<VehicleReport> findAllByWinningApplyApplyRoundIdAndTypeAndState(Long applyRoundId, VehicleReportType type, VehicleReportState state);

    List<VehicleReport> findAllByWinningIdAndState(Long id, VehicleReportState vehicleReportState);
}
