package kea.enter.enterbe.domain.winning.repository;

import kea.enter.enterbe.domain.winning.entity.Winning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WinningRepository extends JpaRepository<Winning, Long> {

}
