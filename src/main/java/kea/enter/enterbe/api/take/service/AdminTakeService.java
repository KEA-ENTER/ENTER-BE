package kea.enter.enterbe.api.take.service;

import kea.enter.enterbe.api.take.controller.dto.response.GetReportListResponse;
import kea.enter.enterbe.api.take.controller.dto.response.GetReturnReportResponse;
import kea.enter.enterbe.api.take.controller.dto.response.GetTakeSituationResponse;
import kea.enter.enterbe.api.take.service.dto.GetReportListServiceDto;
import kea.enter.enterbe.api.take.service.dto.GetReturnReportServiceDto;
import kea.enter.enterbe.api.take.controller.dto.response.GetTakeReportResponse;
import kea.enter.enterbe.api.take.service.dto.GetTakeReportServiceDto;
import kea.enter.enterbe.api.take.service.dto.GetTakeSituationServiceDto;

public interface AdminTakeService {
    GetTakeSituationResponse getTakeSituation(GetTakeSituationServiceDto dto);

    GetReturnReportResponse getReturnReport(GetReturnReportServiceDto dto);

    GetTakeReportResponse getTakeReport(GetTakeReportServiceDto dto);

    GetReportListResponse getReportList(GetReportListServiceDto service);
}
