package kea.enter.enterbe.domain.question.repository;

import kea.enter.enterbe.domain.question.entity.Answer;
import kea.enter.enterbe.domain.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findByQuestionId(Long questionId);
}
