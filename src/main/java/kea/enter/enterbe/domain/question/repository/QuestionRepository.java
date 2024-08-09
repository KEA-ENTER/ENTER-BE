package kea.enter.enterbe.domain.question.repository;

import kea.enter.enterbe.domain.question.entity.Question;
import kea.enter.enterbe.domain.question.entity.QuestionState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    Optional<Question> findByIdAndMemberId(Long questionId, Long memberId);

    Optional<Question> findById(Long questionId);

    Optional<Question> findByIdAndState(Long questionId, QuestionState state);

    Optional<Question> findByIdAndStateNot(Long questionId, QuestionState state);

    // Page로 조회
    Page<Question> findAllByStateNot(QuestionState state, Pageable pageable);
}