package kea.enter.enterbe.api.vehicle.service.dto;

import kea.enter.enterbe.domain.take.entity.VehicleReportType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class PostVehicleReportServiceDto {

    private Long memberId;
    private MultipartFile frontImg;
    private MultipartFile rightImg;
    private MultipartFile backImg;
    private MultipartFile leftImg;
    private MultipartFile dashboardImg;
    private String note;
    private String parkingLoc;
    private VehicleReportType type;

    @Builder
    public PostVehicleReportServiceDto(Long memberId, MultipartFile frontImg,
        MultipartFile rightImg, MultipartFile backImg, MultipartFile leftImg,
        MultipartFile dashboardImg, String note, String parkingLoc,
        VehicleReportType type) {
        this.memberId = memberId;
        this.frontImg = frontImg;
        this.rightImg = rightImg;
        this.backImg = backImg;
        this.leftImg = leftImg;
        this.dashboardImg = dashboardImg;
        this.parkingLoc = parkingLoc;
        this.type = type;
        this.note = note;
    }

    public static PostVehicleReportServiceDto of(Long memberId, MultipartFile frontImg,
        MultipartFile rightImg, MultipartFile backImg, MultipartFile leftImg,
        MultipartFile dashboardImg, String note, String parkingLoc,
        VehicleReportType type) {
        return PostVehicleReportServiceDto.builder()
            .memberId(memberId)
            .frontImg(frontImg)
            .rightImg(rightImg)
            .backImg(backImg)
            .leftImg(leftImg)
            .dashboardImg(dashboardImg)
            .parkingLoc(parkingLoc)
            .type(type)
            .note(note)
            .build();
    }
}
