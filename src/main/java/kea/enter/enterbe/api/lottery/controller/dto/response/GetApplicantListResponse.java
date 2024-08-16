package kea.enter.enterbe.api.lottery.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GetApplicantListResponse {
    @Schema(description = "회차", example = "24")
    private int round;

    @Schema(description = "인수 날짜", example = "2024-08-03")
    private String takeDate;

    @Schema(description = "차량 모델", example = "G80")
    private String vehicleModel;

    @Schema(description = "차량 번호", example = "123가 4568")
    private String vehicleNo;

    private List<ApplicantInfo> applicantList;

    @Schema(description = "현재 페이지", example = "0")
    private int page;

    @Schema(description = "현재 페이지 사이즈", example = "8")
    private int size;

    @Schema(description = "전체 개수", example = "20")
    private long totalElements;

    @Schema(description = "전체 페이지", example = "3")
    private int totalPages;

    @Schema(description = "다음 페이지 존재 여부", example = "true")
    private boolean hasNextPage;

    @Builder
    public GetApplicantListResponse(
        int round, String takeDate, String vehicleModel, String vehicleNo,
        List<ApplicantInfo> applicantList,
        int page, int size, long totalElements, int totalPages, boolean hasNextPage) {
        this.round = round;
        this.takeDate = takeDate;
        this.vehicleModel = vehicleModel;
        this.vehicleNo = vehicleNo;
        this.applicantList = applicantList;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.hasNextPage = hasNextPage;
    }

    public static GetApplicantListResponse of(
        int round, String takeDate, String vehicleModel, String vehicleNo,
        List<ApplicantInfo> applicantList,
        int page, int size, long totalElements, int totalPages, boolean hasNextPage) {
        return GetApplicantListResponse.builder()
            .round(round)
            .takeDate(takeDate)
            .vehicleModel(vehicleModel)
            .vehicleNo(vehicleNo)
            .applicantList(applicantList)
            .page(page)
            .size(size)
            .totalElements(totalElements)
            .totalPages(totalPages)
            .hasNextPage(hasNextPage)
            .build();
    }
}