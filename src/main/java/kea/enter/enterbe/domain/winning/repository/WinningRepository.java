package kea.enter.enterbe.domain.winning.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import kea.enter.enterbe.domain.winning.entity.Winning;
import kea.enter.enterbe.domain.winning.entity.WinningState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WinningRepository extends JpaRepository<Winning, Long> {

    @Query(value = "SELECT w "
        + "FROM Winning w "
        + "left outer join w.apply a "
        + "left outer join a.applyRound ar "
        + "where a.member.id = :memberId "
        + "and ar.takeDate = :takeDate "
        + "and w.state = :state ")
    Optional<Winning> findByMemberIdAndTakeDateAndState(
        @Param("memberId") Long memberId,
        @Param("takeDate") LocalDate takeDate,
        @Param("state") WinningState state
    );

    List<Winning> findAllByApplyApplyRoundIdAndState(Long applyRoundId, WinningState state);
}
