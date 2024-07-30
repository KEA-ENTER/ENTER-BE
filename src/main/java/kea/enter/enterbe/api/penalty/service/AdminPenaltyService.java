package kea.enter.enterbe.api.penalty.service;

import kea.enter.enterbe.api.penalty.service.dto.PostPenaltyServiceDto;

public interface AdminPenaltyService {
    void createPenalty(PostPenaltyServiceDto postPenaltyServiceDto);
}
