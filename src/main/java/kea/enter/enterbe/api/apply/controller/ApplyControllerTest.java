package kea.enter.enterbe.api.apply.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kea.enter.enterbe.api.apply.service.ApplyService;
import kea.enter.enterbe.api.apply.service.ApplyServiceTestImpl;
import kea.enter.enterbe.api.apply.service.dto.DeleteApplyDetailServiceDto;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static kea.enter.enterbe.global.common.api.CustomResponseCode.SUCCESS;

@Tag(name = "[사용자] 신청 관련 API", description = "Apply")
@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
public class ApplyControllerTest {
    private final ApplyServiceTestImpl applyServiceTest;
    @Operation(summary = "당첨자 발표 이전 차량 신청 내역 취소 API", description = "패널티 및 재배정 없음")
    @DeleteMapping("/detail/before/{applyId}")
    public ResponseEntity<String> deleteApplyDetailBefore(
        @PathVariable Long applyId,
        Authentication authentication
    ) {
        Long memberId = Long.valueOf(authentication.getName());
        int result = applyServiceTest.deleteApplyDetail(DeleteApplyDetailServiceDto.of(memberId, applyId));
        if (result==0)
            return ResponseEntity.ok("성공적으로 삭제되었지만, 이후 대기인원이 존재하지 않습니다.");
        else if(result==1)
            return ResponseEntity.ok(SUCCESS.getMessage());
        else
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
    }
    @Operation(summary = "당첨자 발표 이전 차량 신청 내역 취소 API", description = "패널티 및 재배정 없음")
    @DeleteMapping("/detail/after/{applyId}")
    public ResponseEntity<String> deleteApplyDetailAfter(
        @PathVariable Long applyId,
        Authentication authentication
    ) {
        Long memberId = Long.valueOf(authentication.getName());
        int result = applyServiceTest.deleteApplyDetail(DeleteApplyDetailServiceDto.of(memberId, applyId));
        if (result==0)
            return ResponseEntity.ok("성공적으로 삭제되었지만, 이후 대기인원이 존재하지 않습니다.");
        else if(result==1)
            return ResponseEntity.ok(SUCCESS.getMessage());
        else
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
    }
    @Operation(summary = "당첨자 발표 이전 차량 신청 내역 취소 API", description = "패널티 및 재배정 없음")
    @DeleteMapping("/detail/penalty/{applyId}")
    public ResponseEntity<String> deleteApplyDetailPenalty(
        @PathVariable Long applyId,
        Authentication authentication
    ) {
        Long memberId = Long.valueOf(authentication.getName());
        int result = applyServiceTest.deleteApplyDetail(DeleteApplyDetailServiceDto.of(memberId, applyId));
        if (result==0)
            return ResponseEntity.ok("성공적으로 삭제되었지만, 이후 대기인원이 존재하지 않습니다.");
        else if(result==1)
            return ResponseEntity.ok(SUCCESS.getMessage());
        else
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
    }
}
