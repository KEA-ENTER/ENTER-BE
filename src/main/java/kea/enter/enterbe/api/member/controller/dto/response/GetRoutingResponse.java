package kea.enter.enterbe.api.member.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetRoutingResponse {

    @Schema(description = "신청메뉴 라우팅 페이지 번호(1:신청서 작성 페이지 / 2: 신청서 내역 조회 페이지 / 3: 신청 기간 아니에요 페이지 / 4: 추첨 결과 확인 페이지)", example = "1")
    private Integer routingId;
    @Schema(description = "사용자의 상태 : EMPLOYEE(사원), APPLICANT(신청자), WINNER(당첨자), CANDIDATE(대기자), NON_WINNER(미당첨자)", example = "EMPLOYEE")
    private String userState;

    @Builder
    public GetRoutingResponse(Integer routingId, String userState) {
        this.routingId = routingId;
        this.userState = userState;
    }

    public static GetRoutingResponse of(Integer routingId, String userState){
        return GetRoutingResponse.builder()
            .routingId(routingId)
            .userState(userState)
            .build();
    }
}
