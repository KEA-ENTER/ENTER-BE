package kea.enter.enterbe.api.penalty.service;

import kea.enter.enterbe.api.penalty.controller.dto.response.GetPenaltyListResponse;
import kea.enter.enterbe.api.penalty.controller.dto.response.GetPenaltyResponse;
import kea.enter.enterbe.api.penalty.service.dto.GetPenaltyListServiceDto;
import kea.enter.enterbe.api.penalty.service.dto.GetPenaltyServiceDto;
import java.util.List;

public interface PenaltyService {
    GetPenaltyListResponse getPenaltyList(GetPenaltyListServiceDto dto);
    GetPenaltyResponse getPenalty(GetPenaltyServiceDto dto);
}
