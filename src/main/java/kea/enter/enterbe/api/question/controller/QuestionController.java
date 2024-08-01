package kea.enter.enterbe.api.question.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kea.enter.enterbe.api.question.controller.dto.request.QuestionRequestDto;
import kea.enter.enterbe.api.question.controller.dto.response.QuestionResponseDto;
import kea.enter.enterbe.api.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
@Tag(name = "문의사항 작성", description = "문의사항 작성 API")
public class QuestionController {

    private final QuestionService questionService;

    @Operation(summary = "사용자가 작성한 문의사항 저장")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "작성이 완료되었습니다.", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "MEM-ERR-001", description = "멤버가 존재하지 않습니다.", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "GLB-ERR-001", description = "필수 입력칸이 입력되지 않았습니다.", content = @Content(mediaType = "application/json")),
    })
    @PostMapping
    public ResponseEntity<QuestionResponseDto> createQuestion(
        @Valid @RequestBody QuestionRequestDto dto) {

        questionService.createQuestion(dto);

        QuestionResponseDto responseDto = new QuestionResponseDto("작성이 완료되었습니다.");
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
