package kea.enter.enterbe.domain.lottery.repository;

import kea.enter.enterbe.domain.lottery.entity.Waiting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaitingRepository extends JpaRepository<Waiting, Long> {

}
