package kea.enter.enterbe.domain.round.repository;

import kea.enter.enterbe.domain.round.entity.ApplyRound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplyRoundRepository extends JpaRepository<ApplyRound, Long> {

}
