package kea.enter.enterbe.api.vehicle.controller;

import java.nio.charset.StandardCharsets;
import kea.enter.enterbe.ControllerTestSupport;
import org.apache.hc.core5.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class VehicleControllerTest extends ControllerTestSupport {

    @DisplayName(value = "")
    @Test
    public void postTakeVehicleReport() throws Exception {
        //given
        MockMultipartFile image1 = new MockMultipartFile("front_img", "test.jpg",
            ContentType.IMAGE_JPEG.getMimeType(),
            "test file".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile image2 = new MockMultipartFile("right_img", "test.jpg",
            ContentType.IMAGE_JPEG.getMimeType(),
            "test file".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile image3 = new MockMultipartFile("back_img", "test.jpg",
            ContentType.IMAGE_JPEG.getMimeType(),
            "test file".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile image4 = new MockMultipartFile("left_img", "test.jpg",
            ContentType.IMAGE_JPEG.getMimeType(),
            "test file".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile image5 = new MockMultipartFile("dashboard_img", "test.jpg",
            ContentType.IMAGE_JPEG.getMimeType(),
            "test file".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile notice = new MockMultipartFile("note", "note", ContentType.TEXT_PLAIN.getMimeType(),
            "note".getBytes(StandardCharsets.UTF_8));
        //when
        mockMvc.perform(
            multipart("/vehicles/reports/take")
                .file(image1)
                .file(image2)
                .file(image3)
                .file(image4)
                .file(image5)
                .file(notice)
        ).andExpect(status().isOk());
        //then

    }

}