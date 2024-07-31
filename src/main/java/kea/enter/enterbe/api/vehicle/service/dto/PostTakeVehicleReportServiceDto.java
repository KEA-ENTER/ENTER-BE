package kea.enter.enterbe.api.vehicle.service.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class PostTakeVehicleReportServiceDto {

    private Long memberId;
    private MultipartFile frontImg;
    private MultipartFile rightImg;
    private MultipartFile backImg;
    private MultipartFile leftImg;
    private MultipartFile dashboardImg;
    private String note;

    @Builder
    public PostTakeVehicleReportServiceDto(Long memberId, MultipartFile frontImg,
        MultipartFile rightImg, MultipartFile backImg, MultipartFile leftImg,
        MultipartFile dashboardImg, String note) {
        this.memberId = memberId;
        this.frontImg = frontImg;
        this.rightImg = rightImg;
        this.backImg = backImg;
        this.leftImg = leftImg;
        this.dashboardImg = dashboardImg;
        this.note = note;
    }

    public static PostTakeVehicleReportServiceDto of(Long memberId, MultipartFile frontImg,
        MultipartFile rightImg, MultipartFile backImg, MultipartFile leftImg,
        MultipartFile dashboardImg, String note) {
        return PostTakeVehicleReportServiceDto.builder()
            .memberId(memberId)
            .frontImg(frontImg)
            .rightImg(rightImg)
            .backImg(backImg)
            .leftImg(leftImg)
            .dashboardImg(dashboardImg)
            .note(note)
            .build();
    }
}
