package kea.enter.enterbe.domain.apply.repository;

import kea.enter.enterbe.api.lottery.controller.dto.request.ApplicantSearchType;
import kea.enter.enterbe.domain.apply.entity.Apply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApplyCustomRepository {
    // 해당 추첨 회차의 신청 내역을 조회한다
    Page<Apply> findAllApplyByCondition(Long applyRoundId, String keyword, ApplicantSearchType searchType, Pageable pageable);
}