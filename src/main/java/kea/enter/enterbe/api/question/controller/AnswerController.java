package kea.enter.enterbe.api.question.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kea.enter.enterbe.api.question.controller.dto.request.AnswerRequestDto;
import kea.enter.enterbe.api.question.controller.dto.request.GetAnswerRequestDto;
import kea.enter.enterbe.api.question.controller.dto.response.GetAnswerResponseDto;
import kea.enter.enterbe.api.question.service.AnswerService;
import kea.enter.enterbe.api.question.service.dto.GetAnswerServiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static kea.enter.enterbe.global.common.api.CustomResponseCode.SUCCESS;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "문의사항 답변", description = "문의사항 답변 API")
public class AnswerController {

    private final AnswerService answerService;

    @Operation(summary = "문의사항 답변 작성 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "MEM-ERR-001", description = "멤버가 존재하지 않습니다.", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "QST-ERR-001", description = "문의사항이 존재하지 않습니다.", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "QST-ERR-003", description = "답변 완료된 문의사항입니다.", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "QST-ERR-004", description = "삭제된 문의사항입니다.", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "MAIL-ERR-001", description = "메일 생성에 실패했습니다.", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "MAIL-ERR-002", description = "메일 template load에 실패했습니다.", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "MAIL-ERR-003", description = "메일 전송에 실패했습니다.", content = @Content(mediaType = "application/json")),
    })
    @PostMapping("/{questionId}/answers")
    public ResponseEntity<String> createAnswer(
        @PathVariable Long questionId,
        @Valid @RequestBody AnswerRequestDto dto) {

        answerService.answerQuestion(questionId, dto);
        return ResponseEntity.ok(SUCCESS.getMessage());
    }

    @Operation(summary = "문의사항 답변 조회 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = GetAnswerResponseDto.class))),
        @ApiResponse(responseCode = "MEM-ERR-001", description = "멤버가 존재하지 않습니다.", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "QST-ERR-001", description = "문의사항이 존재하지 않습니다.", content = @Content(mediaType = "application/json")),
    })
    @GetMapping("/{questionId}/answers")
    public ResponseEntity<GetAnswerResponseDto> getAnswer(@PathVariable Long questionId,
        @Valid @RequestBody GetAnswerRequestDto dto) {

        GetAnswerResponseDto response =  answerService.getAnswer(
            GetAnswerServiceDto.of(questionId, dto.getMemberId()));
        return ResponseEntity.ok(response);
    }
}
