package kea.enter.enterbe.api.question.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kea.enter.enterbe.api.question.controller.dto.request.QuestionSearchType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetQuestionListServiceDto {

    @Schema(description = "페이지 번호", example = "1")
    private int pageNumber;

    @Schema(description = "검색 키워드", example = "질문")
    private String keyword;

    @Schema(description = "검색 종류 (ALL, CATEGORY, STATE, WRITER)", example = "ALL")
    private QuestionSearchType searchType;

    @Builder
    public GetQuestionListServiceDto(int pageNumber, String keyword, QuestionSearchType searchType) {
        this.pageNumber = pageNumber;
        this.keyword = keyword;
        this.searchType = searchType;
    }

    public static GetQuestionListServiceDto of(int pageNumber, String keyword, QuestionSearchType searchType) {
        return GetQuestionListServiceDto.builder()
            .pageNumber(pageNumber)
            .keyword(keyword)
            .searchType(searchType)
            .build();
    }
}
