package kea.enter.enterbe.api.apply.service;

import kea.enter.enterbe.api.apply.controller.dto.response.GetApplySituationResponse;
import kea.enter.enterbe.api.apply.service.dto.GetApplySituationServiceDto;

public interface AdminApplyService {
    GetApplySituationResponse getApplySituation(GetApplySituationServiceDto dto);
}
