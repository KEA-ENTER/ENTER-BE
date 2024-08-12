package kea.enter.enterbe.domain.apply.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import kea.enter.enterbe.domain.apply.entity.ApplyRound;
import kea.enter.enterbe.domain.apply.entity.ApplyRoundState;
import kea.enter.enterbe.domain.apply.entity.ApplyState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplyRoundRepository extends JpaRepository<ApplyRound, Long>, ApplyRoundCustomRepository {
    Optional<ApplyRound> findByIdAndState(Long applyRoundId, ApplyRoundState state); // 회차 ID를 기준으로 회차를 조회한다
    // 인수 날짜를 기준으로 회차를 조회한다
    List<ApplyRound> findAllByTakeDateBetweenAndState(LocalDate startDate, LocalDate endDate, ApplyRoundState state);
    // 인수, 반납 날짜를 지정하여 신청 회차를 조회
    List<ApplyRound> findAllByTakeDateAndReturnDateAndState(LocalDate takeDate, LocalDate returnDate, ApplyRoundState state);
    List<ApplyRound> findAllApplyRoundsByTakeDateBetweenAndState(LocalDate thisMonday, LocalDate thisSunday, ApplyRoundState applyRoundState);

    List<Long> findIdByStateAndRound(ApplyRoundState state, Integer round);    // 현재 ApplyRound에서 가장 큰 값을 round 값을 반환
    @Query("SELECT MAX(ar.round) FROM ApplyRound ar WHERE ar.state = :state")
    Integer findMaxRoundByState(@Param("state") ApplyRoundState state);

}
