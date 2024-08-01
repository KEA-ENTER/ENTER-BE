package kea.enter.enterbe.api.question.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kea.enter.enterbe.api.penalty.service.dto.DeletePenaltyServiceDto;
import kea.enter.enterbe.api.question.controller.dto.request.QuestionRequestDto;
import kea.enter.enterbe.api.question.controller.dto.response.QuestionResponseDto;
import kea.enter.enterbe.api.question.service.QuestionService;
import kea.enter.enterbe.api.question.service.dto.DeleteQuestionServiceDto;
import kea.enter.enterbe.domain.question.entity.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static kea.enter.enterbe.global.common.api.CustomResponseCode.SUCCESS;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
@Tag(name = "문의사항", description = "문의사항 API")
public class QuestionController {

    private final QuestionService questionService;

    /* 문의사항 작성 API */
    @Operation(summary = "문의사항 작성 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "MEM-ERR-001", description = "멤버가 존재하지 않습니다.", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "GLB-ERR-001", description = "필수 입력칸이 입력되지 않았습니다.", content = @Content(mediaType = "application/json")),
    })
    @PostMapping
    public ResponseEntity<String> createQuestion(
        @Valid @RequestBody QuestionRequestDto dto) {

        questionService.createQuestion(dto);

        QuestionResponseDto responseDto = new QuestionResponseDto("작성이 완료되었습니다.");
        return ResponseEntity.ok(SUCCESS.getMessage());
    }

    /* 문의사항 삭제 API */
    @Operation(summary = "문의사항 삭제 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "MEM-ERR-001", description = "멤버가 존재하지 않습니다.", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "QST-ERR-001", description = "문의사항이 존재하지 않습니다.", content = @Content(mediaType = "application/json")),
    })
    @DeleteMapping("/{memberId}/{questionId}")
    public ResponseEntity<String> deleteQuestion(
        @PathVariable Long memberId,
        @PathVariable Long questionId) {
        // TODO: 어드민 권한 검사
        questionService.deleteQuestion(DeleteQuestionServiceDto.of(memberId, questionId));
        return ResponseEntity.ok(SUCCESS.getMessage());
    }
}
