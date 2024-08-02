package kea.enter.enterbe.domain.take.repository;

import kea.enter.enterbe.domain.take.entity.VehicleReport;
import kea.enter.enterbe.domain.take.entity.VehicleReportState;
import kea.enter.enterbe.domain.take.entity.VehicleReportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VehicleReportRepository extends JpaRepository<VehicleReport,Long> {

    VehicleReport findByWinningIdAndState(Long id, VehicleReportState vehicleReportState);
    List<VehicleReport> findAllByWinningApplyApplyRoundIdAndTypeAndState(Long applyRoundId, VehicleReportType type, VehicleReportState state);
}
