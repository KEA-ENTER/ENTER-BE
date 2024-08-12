package kea.enter.enterbe.api.member.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

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
