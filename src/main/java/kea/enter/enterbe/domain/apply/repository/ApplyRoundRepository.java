package kea.enter.enterbe.domain.apply.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import kea.enter.enterbe.domain.apply.entity.ApplyRound;
import kea.enter.enterbe.domain.apply.entity.ApplyRoundState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplyRoundRepository extends JpaRepository<ApplyRound, Long>, ApplyRoundCustomRepository {
    Optional<ApplyRound> findByIdAndState(Long applyRoundId, ApplyRoundState state); // 회차 ID를 기준으로 회차를 조회한다
    // 인수 날짜를 기준으로 회차를 조회한다
    List<ApplyRound> findAllByTakeDateBetweenAndState(LocalDate startDate, LocalDate endDate, ApplyRoundState state);
    // 인수, 반납 날짜를 지정하여 신청 회차를 조회
    List<ApplyRound> findAllByTakeDateAndReturnDateAndState(LocalDate takeDate, LocalDate returnDate, ApplyRoundState state);
    List<ApplyRound> findAllApplyRoundsByTakeDateBetweenAndState(LocalDate thisMonday, LocalDate thisSunday, ApplyRoundState applyRoundState);

    // 가장 큰 round 값을 찾는 메서드
    Optional<Integer> findMaxRoundByState(ApplyRoundState state);

    List<Long> findIdByStateAndRound(ApplyRoundState state, Integer round);
}
