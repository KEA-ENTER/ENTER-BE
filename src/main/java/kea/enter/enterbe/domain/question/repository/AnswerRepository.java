package kea.enter.enterbe.domain.question.repository;

import kea.enter.enterbe.domain.question.entity.Answer;
import kea.enter.enterbe.domain.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findByQuestionId(Long questionId);
}
