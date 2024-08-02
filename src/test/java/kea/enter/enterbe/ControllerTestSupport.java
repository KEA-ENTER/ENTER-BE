package kea.enter.enterbe;

import com.fasterxml.jackson.databind.ObjectMapper;
import kea.enter.enterbe.api.controller.ex.ExController;
import kea.enter.enterbe.api.penalty.controller.AdminPenaltyController;
import kea.enter.enterbe.api.penalty.service.AdminPenaltyService;
import kea.enter.enterbe.api.service.ex.ExService;
import kea.enter.enterbe.api.vehicle.controller.AdminVehicleController;
import kea.enter.enterbe.api.vehicle.controller.VehicleController;
import kea.enter.enterbe.api.vehicle.service.AdminVehicleService;
import kea.enter.enterbe.api.vehicle.service.VehicleService;
import kea.enter.enterbe.domain.vehicle.repository.VehicleRepository;
import kea.enter.enterbe.global.security.SecurityConfig;
import kea.enter.enterbe.global.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
    ExController.class, SecurityConfig.class, VehicleController.class, AdminPenaltyController.class, AdminVehicleController.class
})
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    protected ExService exService;
    @MockBean
    protected VehicleService vehicleService;
    @MockBean
    protected FileUtil fileUtil;
    @MockBean
    protected AdminPenaltyService adminPenaltyService;
    @MockBean
    protected AdminVehicleService adminVehicleService;
    @MockBean
    protected VehicleRepository vehicleRepository;
}
