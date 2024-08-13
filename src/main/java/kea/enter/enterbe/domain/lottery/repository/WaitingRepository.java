package kea.enter.enterbe.domain.lottery.repository;

import kea.enter.enterbe.domain.apply.entity.Apply;
import kea.enter.enterbe.domain.apply.entity.ApplyRoundState;
import kea.enter.enterbe.domain.lottery.entity.Waiting;
import kea.enter.enterbe.domain.lottery.entity.WaitingState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface WaitingRepository extends JpaRepository<Waiting, Long> {
    int findWaitingNoByApplyIdAndState(Long id, WaitingState waitingState);
    Optional<Waiting> findByApplyIdAndState(Long applyId, WaitingState waitingState);
    Optional<Waiting> findByApplyAndState(Apply apply, WaitingState winningState);
    @Query("SELECT MAX(ar.waitingNo) FROM Waiting ar WHERE ar.state = :state")
    Integer findMaxWaitingNoByState(@Param("state") WaitingState state);
}
