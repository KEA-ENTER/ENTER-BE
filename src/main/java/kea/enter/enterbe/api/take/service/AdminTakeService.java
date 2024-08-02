package kea.enter.enterbe.api.take.service;

import kea.enter.enterbe.api.take.controller.response.GetTakeSituationResponse;
import kea.enter.enterbe.api.take.service.dto.GetTakeSituationServiceDto;

public interface AdminTakeService {
    GetTakeSituationResponse getTakeSituation(GetTakeSituationServiceDto dto);
}
