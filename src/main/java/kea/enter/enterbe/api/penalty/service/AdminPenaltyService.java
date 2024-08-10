package kea.enter.enterbe.api.penalty.service;

import kea.enter.enterbe.api.penalty.controller.dto.response.GetAdminPenaltyListResponse;
import kea.enter.enterbe.api.penalty.service.dto.DeleteAdminPenaltyServiceDto;
import kea.enter.enterbe.api.penalty.service.dto.GetAdminPenaltyListServiceDto;
import kea.enter.enterbe.api.penalty.service.dto.PostAdminPenaltyServiceDto;

import java.util.List;

public interface AdminPenaltyService {
    void createPenalty(PostAdminPenaltyServiceDto postAdminPenaltyServiceDto);
    List<GetAdminPenaltyListResponse> getPenaltyList(
        GetAdminPenaltyListServiceDto getAdminPenaltyListServiceDto);
    void deletePenalty(DeleteAdminPenaltyServiceDto deleteAdminPenaltyServiceDto);
}
