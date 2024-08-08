package kea.enter.enterbe.api.take.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetTakeReportServiceDto {
    private Long winningId;

    @Builder
    public GetTakeReportServiceDto(Long winningId) {
        this.winningId = winningId;
    }

    public static GetTakeReportServiceDto of(Long winningId) {
        return GetTakeReportServiceDto.builder()
            .winningId(builder().winningId)
            .build();
    }
}
