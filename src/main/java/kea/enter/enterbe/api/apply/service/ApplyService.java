package kea.enter.enterbe.api.apply.service;

import kea.enter.enterbe.api.apply.controller.response.GetApplyResponse;
import kea.enter.enterbe.api.apply.controller.response.GetApplyVehicleResponse;
import kea.enter.enterbe.api.apply.service.dto.GetApplyServiceDto;
import kea.enter.enterbe.api.apply.service.dto.GetApplyVehicleServiceDto;
import java.util.List;

public interface ApplyService {
    List<GetApplyResponse> getApply(GetApplyServiceDto dto);
    List<GetApplyVehicleResponse> getApplyVehicle(GetApplyVehicleServiceDto dto);
}
