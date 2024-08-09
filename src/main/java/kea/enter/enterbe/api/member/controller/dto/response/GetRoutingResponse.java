package kea.enter.enterbe.api.member.controller.dto.response;

import lombok.Builder;
import lombok.Getter;
/*
일단은 어떻게 리턴이 갈지 모르겠어서 이걸로 선언해두고
routing Id만 리턴해준다 싶으면 그냥 Integer로 리턴하는게 나음.
 */

@Getter
public class GetRoutingResponse {

    private Integer routingId;
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
