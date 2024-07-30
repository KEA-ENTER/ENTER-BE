package kea.enter.enterbe.api.controller.question;

import jakarta.validation.Valid;
import kea.enter.enterbe.api.controller.question.dto.request.QuestionRequestDto;
import kea.enter.enterbe.api.controller.question.dto.response.QuestionResponseDto;
import kea.enter.enterbe.api.service.question.QuestionService;
import kea.enter.enterbe.domain.question.entity.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    public ResponseEntity<QuestionResponseDto> createQuestion(@Valid @RequestBody QuestionRequestDto dto) {

        questionService.createQuestion(dto);

        QuestionResponseDto responseDto = new QuestionResponseDto("작성이 완료되었습니다.");
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
