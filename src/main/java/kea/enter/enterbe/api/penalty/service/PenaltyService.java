package kea.enter.enterbe.api.penalty.service;

import kea.enter.enterbe.api.penalty.controller.dto.response.GetPenaltyListResponse;
import kea.enter.enterbe.api.penalty.service.dto.GetPenaltyListServiceDto;
import java.util.List;

public interface PenaltyService {
    List<GetPenaltyListResponse> getPenaltyList(GetPenaltyListServiceDto getPenaltyListServiceDto);
}
