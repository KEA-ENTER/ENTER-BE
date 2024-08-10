package kea.enter.enterbe.api.take.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetReturnReportServiceDto {
    private Long winningId;

    @Builder
    public GetReturnReportServiceDto(Long winningId) {
        this.winningId = winningId;
    }

    public static GetReturnReportServiceDto of(Long winningId) {
        return GetReturnReportServiceDto.builder()
            .winningId(winningId)
            .build();
    }
}
