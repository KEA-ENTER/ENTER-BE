package kea.enter.enterbe.api.apply.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kea.enter.enterbe.api.apply.controller.dto.response.GetApplyResponse;
import kea.enter.enterbe.api.apply.service.ApplyService;
import kea.enter.enterbe.api.apply.service.dto.GetApplyServiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.List;

@Tag(name = "신청", description = "사용자 신청 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/apply")
public class ApplyController {

    private final ApplyService applyService;

    @Operation(summary = "신청 가능 날짜 조회 API", description = "신청 가능 날짜를 조회합니다.")
    @GetMapping("")
    public ResponseEntity<List<GetApplyResponse>> getApply() {
        GetApplyServiceDto dto = GetApplyServiceDto.of(LocalDate.now());
        List<GetApplyResponse> response = applyService.getApply(dto);
        return ResponseEntity.ok(response);
    }
}