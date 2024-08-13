package kea.enter.enterbe.api.vehicle.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.List;
import kea.enter.enterbe.ControllerTestSupport;
import kea.enter.enterbe.domain.take.entity.VehicleReportType;
import org.apache.hc.core5.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;

@WithMockUser(roles = "USER")
class VehicleControllerTest extends ControllerTestSupport {

    @DisplayName(value = "보고서를 작성한다. (성공)")
    @WithMockUser(roles = "MEMBER", username = "1")
    @Test
    public void postVehicleReport() throws Exception {
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
        given(fileUtil.isImageFileList(List.of(image1,image2,image3,image4,image5))).willReturn(true);
        //when
        mockMvc.perform(
                multipart("/vehicles/reports")
                    .file(image1)
                    .file(image2)
                    .file(image3)
                    .file(image4)
                    .file(image5)
                    .file(notice)
                    .file("note","note".getBytes())
                    .file("parking_loc","parking_loc".getBytes())
                    .param("type", VehicleReportType.TAKE.name())
            )
            .andDo(print())
            .andExpect(status().isOk());
        //then

    }

}