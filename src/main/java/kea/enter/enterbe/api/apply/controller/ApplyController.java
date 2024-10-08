package kea.enter.enterbe.api.apply.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import jakarta.validation.Valid;
import kea.enter.enterbe.api.apply.controller.dto.request.ModifyApplyDetailRequest;
import kea.enter.enterbe.api.apply.controller.dto.request.PostApplyRequest;
import kea.enter.enterbe.api.apply.controller.dto.response.GetApplyDetailResponse;
import kea.enter.enterbe.api.apply.controller.dto.response.GetApplyResponse;
import kea.enter.enterbe.api.apply.controller.dto.response.GetApplyVehicleResponse;
import kea.enter.enterbe.api.apply.controller.dto.response.PostApplyResponse;
import kea.enter.enterbe.api.apply.service.ApplyService;
import kea.enter.enterbe.api.apply.service.dto.DeleteApplyDetailServiceDto;
import kea.enter.enterbe.api.apply.service.dto.GetApplyDetailServiceDto;
import kea.enter.enterbe.api.apply.service.dto.GetApplyServiceDto;
import kea.enter.enterbe.api.apply.service.dto.GetApplyVehicleServiceDto;
import kea.enter.enterbe.api.apply.service.dto.ModifyApplyDetailServiceDto;
import kea.enter.enterbe.api.apply.service.dto.PostApplyServiceDto;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static kea.enter.enterbe.global.common.api.CustomResponseCode.SUCCESS;

@Tag(name = "[사용자] 신청 관련 API", description = "Apply")
@RequiredArgsConstructor
@RestController
@RequestMapping("/applies")
public class ApplyController {

    private final ApplyService applyService;

    @Operation(summary = "신청 가능 날짜 조회 API", description = "신청 가능 날짜를 조회합니다.")
    @GetMapping("")
    public ResponseEntity<List<GetApplyResponse>> getApply() {
        GetApplyServiceDto dto = GetApplyServiceDto.of(LocalDate.now());
        List<GetApplyResponse> response = applyService.getApply(dto);
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "신청 가능한 차량 목록 조회 API", description = "신청 가능한 차량을 모두 조회합니다.")
    @GetMapping("/vehicles")
    public ResponseEntity<List<GetApplyVehicleResponse>> getApplyVehicles(
        @RequestParam LocalDate takeDate,
        @RequestParam LocalDate returnDate) {
        GetApplyVehicleServiceDto dto = GetApplyVehicleServiceDto.of(takeDate, returnDate);
        List<GetApplyVehicleResponse> response = applyService.getApplyVehicles(dto);
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "차량 신청 내역 조회 API", description = "해당 사용자의 현재 회차의 차량 신청 내역을 조회합니다.")
    @GetMapping("/detail")
    public ResponseEntity<GetApplyDetailResponse> getApplyDetail(
        Authentication authentication
        ) {
        Long memberId = Long.valueOf(authentication.getName());
        GetApplyDetailServiceDto dto = GetApplyDetailServiceDto.of(memberId);
        GetApplyDetailResponse response = applyService.getApplyDetail(dto);
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "차량 신청 내역 수정 API", description = "차량 신청서를 수정합니다.")
    @PatchMapping("/detail/{applyId}")
    public ResponseEntity<String> modifyApplyDetail(
        @PathVariable Long applyId,
        @Valid @RequestBody ModifyApplyDetailRequest request,
        Authentication authentication
        ) {
        Long memberId = Long.valueOf(authentication.getName());
        applyService.modifyApplyDetail(ModifyApplyDetailServiceDto.of(memberId, applyId,
                request.getApplyRoundId(), request.getPurpose()));

        return ResponseEntity.ok(SUCCESS.getMessage());
    }
    @Operation(summary = "차량 신청 내역 취소 API", description = "차량 신청서를 취소합니다.")
    @DeleteMapping("/detail/{applyId}")
    public ResponseEntity<String> deleteApplyDetail(
        @PathVariable Long applyId,
        Authentication authentication
    ) {
        Long memberId = Long.valueOf(authentication.getName());
        int result = applyService.deleteApplyDetail(DeleteApplyDetailServiceDto.of(memberId, applyId));
        if (result==0)
            return ResponseEntity.ok("성공적으로 삭제되었지만, 이후 대기인원이 존재하지 않습니다.");
        else if(result==1)
            return ResponseEntity.ok(SUCCESS.getMessage());
        else
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
    }
    @Operation(summary = "차량 신청 API", description = "차량 대여를 신청합니다.")
    @PostMapping("/vehicles")
    public ResponseEntity<PostApplyResponse> postApply(
        Authentication authentication,
        @Valid @RequestBody PostApplyRequest dto
    ) {
        Long memberId = Long.valueOf(authentication.getName());


        return ResponseEntity.ok(applyService.postApply(PostApplyServiceDto.of(memberId, dto.getApplyRoundId(), dto.getPurpose())));
    }
}