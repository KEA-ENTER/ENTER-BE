package kea.enter.enterbe.domain.apply.repository;

import java.util.List;
import kea.enter.enterbe.domain.apply.entity.Apply;
import kea.enter.enterbe.domain.apply.entity.ApplyRound;
import kea.enter.enterbe.domain.apply.entity.ApplyRoundState;
import kea.enter.enterbe.domain.apply.entity.ApplyState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, Long>, ApplyCustomRepository {
    List<Apply> findAllByApplyRoundIdAndState(Long applyRoundId, ApplyState state);

    @Query("SELECT a.member FROM Apply a WHERE a.applyRound = :applyRound AND a.state = 'ACTIVE' ")
    Integer countByApplyRoundRoundAndStateAndApplyRoundState(int round, ApplyState applyState, ApplyRoundState applyRoundState);

    List<Apply> findAllByMemberIdAndState(Long memberId, ApplyState state);
    Integer countByApplyRoundAndState(ApplyRound applyRound, ApplyState applyRoundState);
    List<Apply> findByApplyRoundIdInAndState(List<Long> applyRoundIds, ApplyState state);
    List<Apply> findByApplyRoundIdInAndMemberIdInAndState(List<Long> applyRoundIds, List<Long> memberIds, ApplyState state);


}
