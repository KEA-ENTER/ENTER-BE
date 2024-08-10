package kea.enter.enterbe.api.question.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetQuestionSearchDto {
    @Schema(description = "검색 키워드", example = "차량")
    private String keyword;

    @NotNull
    @Schema(description = "검색 종류 (ALL, CATEGORY, STATE, WRITER)", example = "ALL")
    private QuestionSearchType searchType;

    @Builder
    public GetQuestionSearchDto(String keyword, QuestionSearchType searchType) {
        this.keyword = keyword;
        this.searchType = searchType;
    }

    public static GetQuestionSearchDto of(String keyword, QuestionSearchType searchType) {
        return GetQuestionSearchDto.builder()
            .keyword(keyword)
            .searchType(searchType)
            .build();
    }

}
