package kea.enter.enterbe.domain.apply.repository;

import kea.enter.enterbe.domain.apply.entity.ApplyRound;
import kea.enter.enterbe.domain.apply.entity.ApplyRoundState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplyRoundRepository extends JpaRepository<ApplyRound, Long>, ApplyRoundCustomRepository {
    Optional<ApplyRound> findByIdAndState(Long applyRoundId, ApplyRoundState state); // 회차 ID를 기준으로 회차를 조회한다
    // 인수 날짜를 기준으로 회차를 조회한다
    List<ApplyRound> findAllByTakeDateBetweenAndState(LocalDate startDate, LocalDate endDate, ApplyRoundState state);

    List<ApplyRound> findAllApplyRoundsByTakeDateBetweenAndState(LocalDate thisMonday, LocalDate thisSunday, ApplyRoundState applyRoundState);

}
