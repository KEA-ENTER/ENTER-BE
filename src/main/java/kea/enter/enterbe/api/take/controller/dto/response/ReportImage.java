package kea.enter.enterbe.api.take.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReportImage {
    // 사용일자(인수 일자, 반납 일자), 보고 시간, 사용자 이름, 계기판 사진, 차량 전면 사진, 차량 후면 사진, 차량 측면 사진 (왼쪽), 차량 측면 사진 (오른쪽), 특이사항
    @Schema(description = "계기판 사진", example = "1")
    private String dashboardImg;

    @Schema(description = "차량 전면 사진", example = "2024-08-03")
    private String frontImg;

    @Schema(description = "차량 후면 사진", example = "2024-08-05")
    private String backImg;

    @Schema(description = "차량 측면 사진 (왼쪽)", example = "2024.07.11 12:22")
    private String leftImg;

    @Schema(description = "차량 측면 사진 (오른쪽)", example = "2024.07.11 12:22")
    private String rightImg;

    @Builder
    public ReportImage(String dashboardImg, String frontImg, String backImg, String leftImg, String rightImg) {
        this.dashboardImg = dashboardImg;
        this.frontImg = frontImg;
        this.backImg = backImg;
        this.leftImg = leftImg;
        this.rightImg = rightImg;
    }

    public static ReportImage of(String dashboardImg, String frontImg, String backImg, String leftImg, String rightImg) {
        return ReportImage.builder()
            .dashboardImg(dashboardImg)
            .frontImg(frontImg)
            .backImg(backImg)
            .leftImg(leftImg)
            .rightImg(rightImg)
            .build();
    }
}