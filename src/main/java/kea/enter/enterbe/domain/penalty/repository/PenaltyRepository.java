package kea.enter.enterbe.domain.penalty.repository;

import kea.enter.enterbe.domain.penalty.entity.Penalty;
import kea.enter.enterbe.domain.penalty.entity.PenaltyState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PenaltyRepository extends JpaRepository<Penalty, Long> {
    // 사용자의 페널티를 조회한다
    Optional<Penalty> findByIdAndMemberIdAndState(Long penaltyId, Long memberId, PenaltyState state);

    // 상태값 없이 사용자의 페널티를 조회한다
    Optional<Penalty> findByIdAndMemberId(Long penaltyId, Long memberId);

    // 사용자의 페널티 목록을 조회한다
    List<Penalty> findAllByMemberIdAndStateOrderByCreatedAt(Long memberId, PenaltyState state);
}
