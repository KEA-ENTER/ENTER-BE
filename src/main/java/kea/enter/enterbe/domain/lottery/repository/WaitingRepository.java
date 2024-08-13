package kea.enter.enterbe.domain.lottery.repository;

import kea.enter.enterbe.domain.apply.entity.Apply;
import kea.enter.enterbe.domain.apply.entity.ApplyRoundState;
import kea.enter.enterbe.domain.lottery.entity.Waiting;
import kea.enter.enterbe.domain.lottery.entity.WaitingState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface WaitingRepository extends JpaRepository<Waiting, Long> {
    int findWaitingNoByApplyIdAndState(Long id, WaitingState waitingState);
    Optional<Waiting> findByApplyIdAndState(Long applyId, WaitingState waitingState);
    Optional<Waiting> findByApplyAndState(Apply apply, WaitingState winningState);
    @Query("SELECT MAX(ar.waitingNo) FROM Waiting ar WHERE ar.state = :state AND ar.apply.id = :applyId")
    Integer findMaxWaitingNoByStateAndApplyId(@Param("state") WaitingState state, @Param("applyId") Long applyId);
    @Query("SELECT ar.apply.id FROM Waiting ar WHERE ar.waitingNo > :waitingNo "
        + "AND ar.state = :state AND ar.apply.applyRound.id = :applyRoundId ORDER BY ar.waitingNo ASC")
    List<Long> findApplyIdsByWaitingNoGreaterThanAndStateAndApplyRoundIdOrderByWaitingNoAsc
            (@Param("applyRoundId") Long applyRoundId,
            @Param("waitingNo") Integer waitingNo,
            @Param("state") WaitingState state);



}
