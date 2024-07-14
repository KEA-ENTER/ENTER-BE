package kea.enter.enterbe.api.controller.ex;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kea.enter.enterbe.api.controller.ex.dto.request.ExampleRequest;
import kea.enter.enterbe.api.controller.ex.dto.response.ExampleResponse;
import kea.enter.enterbe.api.service.ex.ExService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "예제", description = "예제 API 명세서")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ex")
public class ExController {
    private final ExService exService;

    @Operation(summary = "a와 b의 합 저장")
    @PostMapping()
    public ResponseEntity<ExampleResponse> example(@Valid @RequestBody ExampleRequest exampleRequest) {
        return ResponseEntity.ok(exService.example(exampleRequest.toService()));
    }
}
