package kea.enter.enterbe;

import java.time.Clock;
import kea.enter.enterbe.api.apply.service.AdminApplyService;
import kea.enter.enterbe.api.apply.service.ApplyService;
import kea.enter.enterbe.api.lottery.service.AdminLotteryService;
import kea.enter.enterbe.api.member.service.LicenseService;
import kea.enter.enterbe.api.lottery.service.LotteryService;
import kea.enter.enterbe.api.penalty.service.AdminPenaltyService;
import kea.enter.enterbe.api.question.service.AnswerService;
import kea.enter.enterbe.api.question.service.EmailService;
import kea.enter.enterbe.api.question.service.QuestionService;
import kea.enter.enterbe.api.take.service.AdminTakeService;
import kea.enter.enterbe.api.vehicle.service.AdminVehicleService;
import kea.enter.enterbe.api.vehicle.service.VehicleService;
import kea.enter.enterbe.domain.apply.repository.ApplyRepository;
import kea.enter.enterbe.domain.member.repository.MemberRepository;
import kea.enter.enterbe.domain.question.repository.AnswerRepository;
import kea.enter.enterbe.domain.question.repository.QuestionRepository;
import kea.enter.enterbe.domain.vehicle.repository.VehicleNoteRepository;
import kea.enter.enterbe.domain.penalty.repository.PenaltyRepository;
import kea.enter.enterbe.domain.take.repository.VehicleReportRepository;
import kea.enter.enterbe.domain.apply.repository.ApplyRoundRepository;
import kea.enter.enterbe.domain.vehicle.repository.VehicleRepository;
import kea.enter.enterbe.domain.lottery.repository.WinningRepository;
import kea.enter.enterbe.global.config.ClockConfig;
import kea.enter.enterbe.global.config.ObjectStorageConfig;
import kea.enter.enterbe.global.util.FileUtil;
import kea.enter.enterbe.global.util.LicenseValidationUtil;
import kea.enter.enterbe.global.util.ObjectStorageUtil;
import org.junit.jupiter.api.AfterEach;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
public abstract class IntegrationTestSupport {

    @Autowired
    protected ApplyService applyService;
    @Autowired
    protected AdminApplyService adminApplyService;
    @Autowired
    protected AdminPenaltyService adminPenaltyService;
    @Autowired
    protected AnswerService answerService;
    @Autowired
    protected QuestionService questionService;
    @Autowired
    protected AdminTakeService adminTakeService;
    @Autowired
    protected VehicleService vehicleService;
    @Autowired
    protected AdminVehicleService adminVehicleService;
    @Autowired
    protected LicenseService licenseService;

    @Autowired
    protected VehicleRepository vehicleRepository;
    @Autowired
    protected QuestionRepository questionRepository;
    @Autowired
    protected AnswerRepository answerRepository;
    @Autowired
    protected WinningRepository winningRepository;
    @Autowired
    protected MemberRepository memberRepository;
    @Autowired
    protected PenaltyRepository penaltyRepository;
    @Autowired
    protected VehicleNoteRepository vehicleNoteRepository;
    @Autowired
    protected VehicleReportRepository vehicleReportRepository;
    @Autowired
    protected ApplyRepository applyRepository;
    @Autowired
    protected ApplyRoundRepository applyRoundRepository;
    @Autowired
    protected LotteryService lotteryService;
    @Autowired
    protected AdminLotteryService adminLotteryService;
    @Autowired
    protected FileUtil fileUtil;

    @MockBean
    protected Clock clock;
    @MockBean
    protected ClockConfig clockConfig;
    @MockBean
    protected ObjectStorageConfig objectStorageConfig;
    @MockBean
    protected ObjectStorageUtil objectStorageUtil;
    @MockBean
    protected LicenseValidationUtil licenseValidationUtil;
    @MockBean
    protected JavaMailSender mailSender;
    @MockBean
    protected EmailService emailService;
    @MockBean
    protected QuartzUtils quartzUtils;
    @MockBean
    protected QuartzJobInitializer quartzJobInitializer;
    @MockBean
    protected Scheduler scheduler;

    @AfterEach
    void tearDown() {
        answerRepository.deleteAllInBatch();
        questionRepository.deleteAllInBatch();
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

