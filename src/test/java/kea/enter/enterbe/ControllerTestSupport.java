package kea.enter.enterbe;

import com.fasterxml.jackson.databind.ObjectMapper;
import kea.enter.enterbe.api.auth.controller.AuthController;
import kea.enter.enterbe.api.auth.service.AuthService;
import kea.enter.enterbe.api.auth.service.CustomUserDetailsService;
import kea.enter.enterbe.api.apply.controller.AdminApplyController;
import kea.enter.enterbe.api.apply.service.AdminApplyService;
import kea.enter.enterbe.api.question.controller.QuestionController;
import kea.enter.enterbe.api.penalty.controller.AdminPenaltyController;
import kea.enter.enterbe.api.penalty.service.AdminPenaltyService;
import kea.enter.enterbe.api.question.service.QuestionService;
import kea.enter.enterbe.api.take.controller.AdminTakeController;
import kea.enter.enterbe.api.take.service.AdminTakeService;
import kea.enter.enterbe.api.vehicle.controller.AdminVehicleController;
import kea.enter.enterbe.api.vehicle.controller.VehicleController;
import kea.enter.enterbe.api.vehicle.service.AdminVehicleService;
import kea.enter.enterbe.api.vehicle.service.VehicleService;
import kea.enter.enterbe.domain.apply.repository.ApplyRoundRepository;
import kea.enter.enterbe.domain.vehicle.repository.VehicleRepository;
import kea.enter.enterbe.global.security.JwtUtil;
import kea.enter.enterbe.global.security.SecurityConfig;
import kea.enter.enterbe.global.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
    AuthController.class,
    AdminApplyController.class,
    AdminPenaltyController.class,
    QuestionController.class,
    AdminTakeController.class,
    VehicleController.class, AdminVehicleController.class
})
@Import({SecurityConfig.class})
public abstract class ControllerTestSupport {
    @MockBean
    protected AuthService authService;

    @MockBean
    protected AdminApplyService adminApplyService;

    @MockBean
    protected AdminPenaltyService adminPenaltyService;

    @MockBean
    protected QuestionService questionService;

    @MockBean
    protected AdminTakeService adminTakeService;

    @MockBean
    protected VehicleService vehicleService;

    @MockBean
    protected QuestionService questionService;
    @MockBean
    protected AdminApplyService adminApplyService;
    @MockBean
    protected VehicleRepository vehicleRepository;

    @MockBean
    protected AdminVehicleService adminVehicleService;
    @MockBean
    protected CustomUserDetailsService customUserDetailsService;

    @MockBean
    protected FileUtil fileUtil;

    @MockBean
    protected JwtUtil jwtUtil;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected ApplyRoundRepository applyRoundRepository;
}
