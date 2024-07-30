package kea.enter.enterbe.api.service.penalty;

import kea.enter.enterbe.api.service.penalty.dto.PenaltyDto;

public interface AdminPenaltyService {
    void createPenalty(Long memberId, PenaltyDto penaltyDto);
}
