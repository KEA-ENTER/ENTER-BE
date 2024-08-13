package kea.enter.enterbe.api.apply.service;

import java.util.List;
import kea.enter.enterbe.api.apply.controller.dto.response.GetApplyDetailResponse;
import kea.enter.enterbe.api.apply.controller.dto.response.GetApplyResponse;
import kea.enter.enterbe.api.apply.controller.dto.response.GetApplyVehicleResponse;
import kea.enter.enterbe.api.apply.controller.dto.response.PostApplyResponse;
import kea.enter.enterbe.api.apply.service.dto.DeleteApplyDetailServiceDto;
import kea.enter.enterbe.api.apply.service.dto.GetApplyDetailServiceDto;
import kea.enter.enterbe.api.apply.service.dto.GetApplyServiceDto;
import kea.enter.enterbe.api.apply.service.dto.GetApplyVehicleServiceDto;
import kea.enter.enterbe.api.apply.service.dto.ModifyApplyDetailServiceDto;
import kea.enter.enterbe.api.apply.service.dto.PostApplyServiceDto;

public interface ApplyService {
    List<GetApplyResponse> getApply(GetApplyServiceDto dto);
    List<GetApplyVehicleResponse> getApplyVehicles(GetApplyVehicleServiceDto dto);
    GetApplyDetailResponse getApplyDetail(GetApplyDetailServiceDto dto);
    void modifyApplyDetail(ModifyApplyDetailServiceDto dto);
    void deleteApplyDetail(DeleteApplyDetailServiceDto dto);
    PostApplyResponse postApply(PostApplyServiceDto dto);

    }
