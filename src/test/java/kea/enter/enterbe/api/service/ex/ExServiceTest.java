package kea.enter.enterbe.api.service.ex;

import kea.enter.enterbe.IntegrationTestSupport;
import kea.enter.enterbe.api.controller.ex.dto.response.ExampleResponse;
import kea.enter.enterbe.api.service.ex.dto.ExampleDto;
import kea.enter.enterbe.domain.ex.entity.ExEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ExServiceTest extends IntegrationTestSupport {

    @DisplayName(value="두 수를 더한 후 값을 저장한다.")
    @Test
    public void example() {
        //given
        Long a = 2L;
        Long b = 3L;
        Long sum = a+b;
        ExampleDto dto = ExampleDto.of(a,b);

        //when
        ExampleResponse response = exService.example(dto);
        List<ExEntity> exEntity = exRepository.findAll();
        //then
        assertThat(response).isNotNull();
        assertThat(response).extracting("sum").isEqualTo(sum);
        assertThat(exEntity).hasSize(1)
                .extracting("sum")
                .isEqualTo(Collections.singletonList(sum));
    }

}