package kea.enter.enterbe.api.apply.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteApplyDetailServiceDto {
    private Long memberId;
    private Long applyId;

    @Builder
    public DeleteApplyDetailServiceDto(Long memberId, Long applyId){
        this.memberId = memberId;
        this.applyId = applyId;
    }

    public static DeleteApplyDetailServiceDto of(Long memberId, Long applyId){
        return DeleteApplyDetailServiceDto.builder()
            .memberId(memberId)
            .applyId(applyId)
            .build();
    }

}
