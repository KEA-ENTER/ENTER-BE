package kea.enter.enterbe.api.controller.ex;

import kea.enter.enterbe.ControllerTestSupport;
import kea.enter.enterbe.api.controller.ex.dto.request.ExampleRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ExControllerTest extends ControllerTestSupport {

    @DisplayName(value="a와 b의 합을 저장한다.")
    @Test
    public void example() throws Exception {
        //given
        Long a = 1L;
        Long b = 2L;
        ExampleRequest request = ExampleRequest.of(a,b);
        //when
        mockMvc.perform(
                        post("/api/ex")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }
}