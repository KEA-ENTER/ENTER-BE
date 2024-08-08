package kea.enter.enterbe.api.take.service;

import kea.enter.enterbe.api.take.controller.dto.response.GetTakeReportResponse;
import kea.enter.enterbe.api.take.controller.dto.response.GetTakeSituationResponse;
import kea.enter.enterbe.api.take.service.dto.GetTakeReportServiceDto;
import kea.enter.enterbe.api.take.service.dto.GetTakeSituationServiceDto;

public interface AdminTakeService {
    GetTakeSituationResponse getTakeSituation(GetTakeSituationServiceDto dto);

    GetTakeReportResponse getTakeReport(GetTakeReportServiceDto dto);
}
