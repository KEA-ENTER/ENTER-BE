package kea.enter.enterbe.api.member.service;

import kea.enter.enterbe.api.member.controller.dto.response.GetMemberPostReportTypeResponse;
import kea.enter.enterbe.api.member.controller.dto.response.GetMemberScoreResponse;
import kea.enter.enterbe.api.member.controller.dto.response.GetRoutingResponse;

public interface MemberService {

    GetMemberScoreResponse getMemberScorePercent(Long memberId);

    GetMemberPostReportTypeResponse getMemberPostReportType(Long memberId);

    GetRoutingResponse getRoutingInformation(Long memberId);
}
