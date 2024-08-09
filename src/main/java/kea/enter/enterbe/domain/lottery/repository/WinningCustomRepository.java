package kea.enter.enterbe.domain.lottery.repository;

import kea.enter.enterbe.api.take.controller.dto.request.ReportSearchType;
import kea.enter.enterbe.domain.lottery.entity.Winning;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WinningCustomRepository {
    // 당첨 내역을 조회한다
    Page<Winning> findAllWinningByCondition(String keyword, ReportSearchType searchType, Pageable pageable);
}