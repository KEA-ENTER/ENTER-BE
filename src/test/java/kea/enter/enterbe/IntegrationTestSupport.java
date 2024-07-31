package kea.enter.enterbe;

import java.time.Clock;
import kea.enter.enterbe.api.penalty.service.AdminPenaltyService;
import kea.enter.enterbe.api.service.ex.ExService;
import kea.enter.enterbe.api.vehicle.service.AdminVehicleService;
import kea.enter.enterbe.api.vehicle.service.VehicleService;
import kea.enter.enterbe.domain.apply.repository.ApplyRepository;
import kea.enter.enterbe.domain.ex.repository.ExRepository;
import kea.enter.enterbe.domain.member.repository.MemberRepository;
import kea.enter.enterbe.domain.note.repository.VehicleNoteRepository;
import kea.enter.enterbe.domain.penalty.repository.PenaltyRepository;
import kea.enter.enterbe.domain.report.repository.VehicleReportRepository;
import kea.enter.enterbe.domain.round.repository.ApplyRoundRepository;
import kea.enter.enterbe.domain.vehicle.repository.VehicleRepository;
import kea.enter.enterbe.domain.winning.repository.WinningRepository;
import kea.enter.enterbe.global.config.ClockConfig;
import kea.enter.enterbe.global.config.ObjectStorageConfig;
import kea.enter.enterbe.global.util.FileUtil;
import kea.enter.enterbe.global.util.ObjectStorageUtil;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public abstract class IntegrationTestSupport {

    @Autowired
    protected ExService exService;
    @Autowired
    protected ExRepository exRepository;
    @Autowired
    protected WinningRepository winningRepository;
    @Autowired
    protected ApplyRepository applyRepository;
    @Autowired
    protected ApplyRoundRepository applyRoundRepository;
    @Autowired
    protected VehicleReportRepository vehicleReportRepository;
    @Autowired
    protected VehicleNoteRepository vehicleNoteRepository;
    @Autowired
    protected VehicleRepository vehicleRepository;
    @Autowired
    protected MemberRepository memberRepository;
    @Autowired
    protected PenaltyRepository penaltyRepository;
    @Autowired
    protected VehicleService vehicleService;
    @Autowired
    protected FileUtil fileUtil;
    @Autowired
    protected AdminPenaltyService adminPenaltyService;
    @MockBean
    protected ObjectStorageConfig objectStorageConfig;
    @MockBean
    protected ObjectStorageUtil objectStorageUtil;
    @MockBean
    protected Clock clock;
    @MockBean
    protected ClockConfig clockConfig;
    @MockBean
    protected AdminVehicleService adminVehicleService;

    @AfterEach
    void tearDown() {
        exRepository.deleteAllInBatch();
        vehicleNoteRepository.deleteAllInBatch();
        vehicleReportRepository.deleteAllInBatch();
        winningRepository.deleteAllInBatch();
        applyRepository.deleteAllInBatch();
        applyRoundRepository.deleteAllInBatch();
        vehicleRepository.deleteAllInBatch();
        penaltyRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }
}
