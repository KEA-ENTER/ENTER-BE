package kea.enter.enterbe.api.member.service;

import kea.enter.enterbe.api.member.controller.dto.response.GetMemberScoreResponse;
import kea.enter.enterbe.global.common.api.CustomResponseCode;

public interface MemberService {

    GetMemberScoreResponse getMemberScorePercent(Long memberId);
}
