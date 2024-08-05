package kea.enter.enterbe.api.apply.service;

import kea.enter.enterbe.api.apply.controller.response.GetApplyResponse;
import kea.enter.enterbe.api.apply.controller.response.GetApplySituationResponse;
import kea.enter.enterbe.api.apply.service.dto.GetApplyServiceDto;
import kea.enter.enterbe.api.apply.service.dto.GetApplySituationServiceDto;
import java.util.List;

public interface ApplyService {
    List<GetApplyResponse> getApply(GetApplyServiceDto dto);
}
