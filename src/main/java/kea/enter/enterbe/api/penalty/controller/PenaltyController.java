package kea.enter.enterbe.api.penalty.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kea.enter.enterbe.api.penalty.controller.dto.response.GetPenaltyListResponse;
import kea.enter.enterbe.api.penalty.service.PenaltyService;
import kea.enter.enterbe.api.penalty.service.dto.GetPenaltyListServiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "패널티 조회", description = "[User] 패널티 조회")
@RestController
@RequiredArgsConstructor
@RequestMapping("/penalties")
public class PenaltyController {
    private final PenaltyService penaltyService;

    @Operation(summary = "패널티 목록 조회 API")
    @GetMapping("")
    public List<GetPenaltyListResponse> getPenaltyList() {
        // TODO 인증
        return penaltyService.getPenaltyList(GetPenaltyListServiceDto.of(1l));
    }
}
