package kea.enter.enterbe.domain.report.repository;

import kea.enter.enterbe.domain.report.entity.VehicleReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleReportRepository extends JpaRepository<VehicleReport,Long> {

}
