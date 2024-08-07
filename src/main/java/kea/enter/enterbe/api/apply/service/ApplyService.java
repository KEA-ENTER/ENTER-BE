package kea.enter.enterbe.api.apply.service;

import java.util.List;
import kea.enter.enterbe.api.apply.controller.dto.response.GetApplyResponse;
import kea.enter.enterbe.api.apply.controller.dto.response.GetApplyVehicleResponse;
import kea.enter.enterbe.api.apply.service.dto.GetApplyServiceDto;
import kea.enter.enterbe.api.apply.service.dto.GetApplyVehicleServiceDto;

public interface ApplyService {
    List<GetApplyResponse> getApply(GetApplyServiceDto dto);
    List<GetApplyVehicleResponse> getApplyVehicles(GetApplyVehicleServiceDto dto);
}
