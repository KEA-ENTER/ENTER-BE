package kea.enter.enterbe.api.penalty.service;

import kea.enter.enterbe.api.penalty.service.dto.PenaltyDto;

public interface AdminPenaltyService {
    void createPenalty(Long memberId, PenaltyDto penaltyDto);
}
