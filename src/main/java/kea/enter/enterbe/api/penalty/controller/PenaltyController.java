package kea.enter.enterbe.api.penalty.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kea.enter.enterbe.api.penalty.controller.dto.response.GetPenaltyListResponse;
import kea.enter.enterbe.api.penalty.controller.dto.response.GetPenaltyResponse;
import kea.enter.enterbe.api.penalty.service.PenaltyService;
import kea.enter.enterbe.api.penalty.service.dto.GetPenaltyListServiceDto;
import kea.enter.enterbe.api.penalty.service.dto.GetPenaltyServiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "패널티 조회", description = "[User] 패널티 조회")
@RestController
@RequiredArgsConstructor
@RequestMapping("/penalties")
public class PenaltyController {
    private final PenaltyService penaltyService;

    @Operation(summary = "패널티 목록 조회 API",
        parameters = {
            @Parameter(name = "Authorization", description = "Bearer Token", required = true, in = ParameterIn.HEADER, schema = @Schema(type = "string"))
        })
    @GetMapping("")
    public List<GetPenaltyListResponse> getPenaltyList(Authentication authentication) {
        return penaltyService.getPenaltyList(GetPenaltyListServiceDto.of(Long.valueOf(authentication.getName())));
    }

    @Operation(summary = "패널티 상세 정보 조회 API",
        parameters = {
            @Parameter(name = "Authorization", description = "Bearer Token", required = true, in = ParameterIn.HEADER, schema = @Schema(type = "string"))
        })
    @GetMapping("/{penaltyId}")
    public GetPenaltyResponse getPenalty(
        Authentication authentication,
        @PathVariable Long penaltyId) {

        return penaltyService.getPenalty(GetPenaltyServiceDto.of(Long.valueOf(authentication.getName()), penaltyId));
    }
}
