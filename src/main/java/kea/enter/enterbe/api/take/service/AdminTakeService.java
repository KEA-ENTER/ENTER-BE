package kea.enter.enterbe.api.take.service;

import kea.enter.enterbe.api.take.controller.dto.response.GetReturnReportResponse;
import kea.enter.enterbe.api.take.controller.dto.response.GetTakeSituationResponse;
import kea.enter.enterbe.api.take.service.dto.GetReturnReportServiceDto;
import kea.enter.enterbe.api.take.service.dto.GetTakeSituationServiceDto;

public interface AdminTakeService {
    GetTakeSituationResponse getTakeSituation(GetTakeSituationServiceDto dto);
    GetReturnReportResponse getReturnReport(GetReturnReportServiceDto dto);
}
