package kea.enter.enterbe.domain.question.repository;

import kea.enter.enterbe.api.question.controller.dto.request.QuestionSearchType;
import kea.enter.enterbe.domain.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomQuestionRepository {
    Page<Question> searchQuestions(String keyword, QuestionSearchType searchType, Pageable pageable);
}