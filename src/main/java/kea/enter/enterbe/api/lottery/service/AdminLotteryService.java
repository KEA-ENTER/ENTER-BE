package kea.enter.enterbe.api.lottery.service;

import kea.enter.enterbe.api.lottery.controller.dto.response.GetApplicantListResponse;
import kea.enter.enterbe.api.lottery.controller.dto.response.GetLotteryListResponse;
import kea.enter.enterbe.api.lottery.service.dto.GetApplicantListServiceDto;
import kea.enter.enterbe.api.lottery.service.dto.GetLotteryListServiceDto;

public interface AdminLotteryService {
    GetLotteryListResponse getLotteryList(GetLotteryListServiceDto dto);

    GetApplicantListResponse getApplicantList(GetApplicantListServiceDto dto);
}