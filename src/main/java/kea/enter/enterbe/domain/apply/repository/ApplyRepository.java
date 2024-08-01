package kea.enter.enterbe.domain.apply.repository;

import kea.enter.enterbe.domain.apply.entity.Apply;
import kea.enter.enterbe.domain.apply.entity.ApplyState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, Long> {
    List<Apply> findAllByApplyRoundIdAndState(Long applyRoundId, ApplyState state);
}
