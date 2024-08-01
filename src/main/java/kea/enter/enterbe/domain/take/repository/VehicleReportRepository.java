package kea.enter.enterbe.domain.take.repository;

import kea.enter.enterbe.domain.take.entity.VehicleReport;
import kea.enter.enterbe.domain.take.entity.VehicleReportState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleReportRepository extends JpaRepository<VehicleReport,Long> {

    VehicleReport findByWinningIdAndState(Long id, VehicleReportState vehicleReportState);
}
