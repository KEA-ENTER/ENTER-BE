package kea.enter.enterbe.domain.apply.repository;

import kea.enter.enterbe.api.lottery.controller.dto.request.LotterySearchType;
import kea.enter.enterbe.domain.apply.entity.ApplyRound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApplyRoundCustomRepository {
    // 그동안의 추첨 회차를 조회한다
    Page<ApplyRound> findAllApplyRoundByCondition(String keyword, LotterySearchType searchType, Pageable pageable);
}
