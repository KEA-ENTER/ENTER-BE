package kea.enter.enterbe.api.penalty.service;

import kea.enter.enterbe.api.penalty.controller.response.GetPenaltyListResponse;
import kea.enter.enterbe.api.penalty.service.dto.GetPenaltyListServiceDto;
import kea.enter.enterbe.api.penalty.service.dto.PostPenaltyServiceDto;

import java.util.List;

public interface AdminPenaltyService {
    void createPenalty(PostPenaltyServiceDto postPenaltyServiceDto);
    List<GetPenaltyListResponse> getPenaltyList(GetPenaltyListServiceDto of);
}
