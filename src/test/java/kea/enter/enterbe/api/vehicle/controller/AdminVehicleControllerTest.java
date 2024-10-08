package kea.enter.enterbe.api.vehicle.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import kea.enter.enterbe.ControllerTestSupport;
import kea.enter.enterbe.api.vehicle.controller.dto.request.PostAdminVehicleRequest;
import kea.enter.enterbe.domain.vehicle.entity.VehicleFuel;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import org.apache.hc.core5.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;

@WebMvcTest(controllers = AdminVehicleController.class)
class AdminVehicleControllerTest extends ControllerTestSupport {

    @DisplayName(value = "법인 차량 추가 컨트롤러 테스트 (성공)")
    @Test
    @WithMockUser(roles = "ADMIN")
    void createVehicle() throws Exception {
        // given
        String vehicleNo = "12가3456";
        String company = "현대";
        String model = "그랜저";
        int seats = 5;
        VehicleFuel fuel = VehicleFuel.GASOLINE;
        MockMultipartFile image = new MockMultipartFile("image", "test.jpg",
            ContentType.IMAGE_JPEG.getMimeType(), "test file".getBytes(StandardCharsets.UTF_8));
        VehicleState state = VehicleState.AVAILABLE;

        PostAdminVehicleRequest request = new PostAdminVehicleRequest(vehicleNo, company, model, seats, fuel, state);

        MockMultipartFile data = new MockMultipartFile("data", "data.json",
            MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8));

        given(fileUtil.isImageFile(image)).willReturn(true);

        // when
        mockMvc.perform(
            multipart("/admin/vehicles")
                .file(image)
                .file(data)
        ).andExpect(status().isOk());

        // then
    }

    @DisplayName(value = "법인 차량 수정 컨트롤러 (성공)")
    @Test
    public void modifyVehicle() throws Exception {
        // given
        Long id = 1l;
        String vehicleNo = "12가3455";
        String company = "현대";
        String model = "그랜저";
        int seats = 4;
        VehicleFuel fuel = VehicleFuel.GASOLINE;

        MockMultipartFile image = new MockMultipartFile("image", "test.jpg",
            ContentType.IMAGE_JPEG.getMimeType(), "test file".getBytes(StandardCharsets.UTF_8));
        VehicleState state = VehicleState.AVAILABLE;

        PostAdminVehicleRequest request = new PostAdminVehicleRequest(id, vehicleNo, company, model, seats, fuel, state);

        MockMultipartFile data = new MockMultipartFile("data", "data.json",
            MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8));

        given(fileUtil.isImageFile(image)).willReturn(true);

        // when
        mockMvc.perform(
            multipart("/admin/vehicles")
                .file(image)
                .file(data)
                .with(req -> {
                    req.setMethod("PATCH");
                    return req;
                })
        ).andExpect(status().isOk());

        // then
    }
}
