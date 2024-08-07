package kea.enter.enterbe.domain.lottery.repository;

import kea.enter.enterbe.domain.lottery.entity.Waiting;
import kea.enter.enterbe.domain.lottery.entity.WaitingState;
import kea.enter.enterbe.domain.lottery.entity.WinningState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    int findWaitingNoByApplyIdAndState(Long id, WaitingState winningState);
}
