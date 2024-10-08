package kea.enter.enterbe.domain.lottery.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kea.enter.enterbe.api.lottery.service.dto.WeightDto;
import kea.enter.enterbe.domain.apply.entity.Apply;
import kea.enter.enterbe.domain.lottery.entity.Winning;
import kea.enter.enterbe.domain.lottery.entity.WinningState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WinningRepository extends JpaRepository<Winning, Long>, WinningCustomRepository {

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

    @Query(value = "SELECT w "
        + "FROM Winning w "
        + "left outer join w.apply a "
        + "left outer join a.applyRound ar "
        + "where a.member.id = :memberId "
        + "and ar.returnDate = :returnDate "
        + "and w.state = :state ")
    Optional<Winning> findByMemberIdAndReturnDateAndState(
        @Param("memberId") Long memberId,
        @Param("returnDate") LocalDate returnDate,
        @Param("state") WinningState state
    );

    List<Winning> findAllByApplyApplyRoundIdAndState(Long applyRoundId, WinningState state);

    @Query("SELECT new kea.enter.enterbe.api.lottery.service.dto.WeightDto(w.apply.member.id, COUNT(w.id)) " +
        "FROM Winning w " +
        "WHERE w.state = 'ACTIVE' " +
        "AND w.createdAt BETWEEN :startDate AND :endDate " +
        "GROUP BY w.id")
    List<WeightDto> countActiveEntitiesByHalfYearAndState(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    Integer countByApplyApplyRoundRoundAndState(int round, WinningState winningState);

    List<Winning> findAllByApplyApplyRoundRoundAndState(int round, WinningState winningState);

    Optional<Winning> findByApplyIdAndState(Long applyId, WinningState state);

    Optional<Winning> findByApplyMemberIdAndApplyApplyRoundTakeDateBetweenAndState(Long memberId, LocalDate start, LocalDate end, WinningState winningState);

    List<Winning> findAllByStateAndApplyApplyRoundReturnDate(WinningState state, LocalDate returnDate);

    Optional<Winning> findByApplyAndState(Apply apply, WinningState state);
}
