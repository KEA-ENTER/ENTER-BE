package kea.enter.enterbe.domain.question.repository;

import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.penalty.entity.Penalty;
import kea.enter.enterbe.domain.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findByIdAndMemberId(Long penaltyId, Long memberId);
}