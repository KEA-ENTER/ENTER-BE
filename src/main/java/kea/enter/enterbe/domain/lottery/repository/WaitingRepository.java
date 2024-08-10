package kea.enter.enterbe.domain.lottery.repository;

import kea.enter.enterbe.domain.apply.entity.Apply;
import kea.enter.enterbe.domain.lottery.entity.Waiting;
import kea.enter.enterbe.domain.lottery.entity.WaitingState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface WaitingRepository extends JpaRepository<Waiting, Long> {
    int findWaitingNoByApplyIdAndState(Long id, WaitingState winningState);

    Optional<Waiting> findByApplyAndState(Apply apply, WaitingState winningState);
}