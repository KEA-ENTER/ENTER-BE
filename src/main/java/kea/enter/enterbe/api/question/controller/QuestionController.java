package kea.enter.enterbe.api.question.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kea.enter.enterbe.api.question.controller.dto.request.QuestionRequestDto;
import kea.enter.enterbe.api.question.service.QuestionService;
import kea.enter.enterbe.api.question.service.dto.CreateQuestionServiceDto;
import kea.enter.enterbe.api.question.service.dto.DeleteQuestionServiceDto;
import kea.enter.enterbe.api.question.service.dto.ModifyQuestionServiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    @Operation(summary = "문의사항 작성 API",
        parameters = {
            @Parameter(name = "Authorization", description = "Bearer Token", required = true, in = ParameterIn.HEADER, schema = @Schema(type = "string"))
        })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "MEM-ERR-001", description = "멤버가 존재하지 않습니다.", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "GLB-ERR-001", description = "필수 입력칸이 입력되지 않았습니다.", content = @Content(mediaType = "application/json")),
    })
    @PostMapping
    public ResponseEntity<String> createQuestion(
        Authentication authentication,
        @Valid @RequestBody QuestionRequestDto dto) {

        Long memberId = Long.valueOf(authentication.getName());
        questionService.createQuestion(CreateQuestionServiceDto.of(memberId, dto.getContent(), dto.getCategory()));

        return ResponseEntity.ok(SUCCESS.getMessage());
    }

    /* 문의사항 삭제 API */
    @Operation(summary = "문의사항 삭제 API",
        parameters = {
            @Parameter(name = "Authorization", description = "Bearer Token", required = true, in = ParameterIn.HEADER, schema = @Schema(type = "string"))
        })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "MEM-ERR-001", description = "멤버가 존재하지 않습니다.", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "QST-ERR-001", description = "문의사항이 존재하지 않습니다.", content = @Content(mediaType = "application/json")),
    })
    @DeleteMapping("/{questionId}")
    public ResponseEntity<String> deleteQuestion(
        Authentication authentication,
        @PathVariable Long questionId) {

        Long memberId = Long.valueOf(authentication.getName());
        questionService.deleteQuestion(DeleteQuestionServiceDto.of(memberId, questionId));
        return ResponseEntity.ok(SUCCESS.getMessage());
    }

    /* 문의사항 수정 API */
    @Operation(summary = "문의사항 수정 API",
        parameters = {
            @Parameter(name = "Authorization", description = "Bearer Token", required = true, in = ParameterIn.HEADER, schema = @Schema(type = "string"))
        })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "MEM-ERR-001", description = "멤버가 존재하지 않습니다.", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "GLB-ERR-001", description = "필수 입력칸이 입력되지 않았습니다.", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "QST-ERR-001", description = "문의사항이 존재하지 않습니다.", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "QST-ERR-002", description = "수정할 수 없는 문의 사항입니다.", content = @Content(mediaType = "application/json")),
    })
    @PatchMapping("/{questionId}")
    public ResponseEntity<String> modifyQuestion(
        Authentication authentication,
        @Valid @RequestBody QuestionRequestDto questionDto,
        @PathVariable Long questionId) {

        Long memberId = Long.valueOf(authentication.getName());
        questionService.modifyQuestion(
            ModifyQuestionServiceDto.of(memberId, questionId,
                questionDto.getContent(), questionDto.getCategory()));

        return ResponseEntity.ok(SUCCESS.getMessage());
    }
}
