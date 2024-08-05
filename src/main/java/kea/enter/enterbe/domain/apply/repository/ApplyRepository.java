package kea.enter.enterbe.domain.apply.repository;

import java.util.List;
import kea.enter.enterbe.domain.apply.entity.Apply;
import kea.enter.enterbe.domain.apply.entity.ApplyState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, Long> {
    List<Apply> findAllByApplyRoundIdAndState(Long applyRoundId, ApplyState state);

    @Query("SELECT a.member.id FROM Apply a WHERE a.applyRound.id = :applyRoundId")
    List<Long> findMemberIdsByApplyRoundId(@Param("applyRoundId") Long applyRoundId);
}
