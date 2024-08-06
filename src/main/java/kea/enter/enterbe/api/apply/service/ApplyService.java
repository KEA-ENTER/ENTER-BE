package kea.enter.enterbe.api.apply.service;

import kea.enter.enterbe.api.apply.controller.dto.response.GetApplyResponse;
import kea.enter.enterbe.api.apply.service.dto.GetApplyServiceDto;
import java.util.List;

public interface ApplyService {
    List<GetApplyResponse> getApply(GetApplyServiceDto dto);
}
