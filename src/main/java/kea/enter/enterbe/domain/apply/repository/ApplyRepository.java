package kea.enter.enterbe.domain.apply.repository;

import java.util.List;
import java.util.Optional;
import kea.enter.enterbe.domain.apply.entity.Apply;
import kea.enter.enterbe.domain.apply.entity.ApplyRound;
import kea.enter.enterbe.domain.apply.entity.ApplyRoundState;
import kea.enter.enterbe.domain.apply.entity.ApplyState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, Long>, ApplyCustomRepository {
    List<Apply> findAllByApplyRoundIdAndState(Long applyRoundId, ApplyState state);
    Integer countByApplyRoundRoundAndStateAndApplyRoundState(int round, ApplyState state, ApplyRoundState applyRoundState);
    List<Apply> findAllByMemberIdAndState(Long memberId, ApplyState state);
    List<Apply> findByApplyRoundIdInAndState(List<Long> applyRoundIds, ApplyState state);
    List<Apply> findByApplyRoundIdInAndMemberIdInAndState(List<Long> applyRoundIds, List<Long> memberIds, ApplyState state);
    Integer countByApplyRoundAndState(ApplyRound applyRound, ApplyState applyState);
    Optional<Apply> findByIdAndState(Long applyId, ApplyState applyState);
}
