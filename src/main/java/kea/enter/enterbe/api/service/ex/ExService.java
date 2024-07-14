package kea.enter.enterbe.api.service.ex;

import kea.enter.enterbe.api.controller.ex.dto.response.ExampleResponse;
import kea.enter.enterbe.api.service.ex.dto.ExampleDto;

public interface ExService {
    ExampleResponse example(ExampleDto service);
}
