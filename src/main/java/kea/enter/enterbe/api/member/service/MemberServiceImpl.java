package kea.enter.enterbe.api.member.service;

import kea.enter.enterbe.api.member.controller.dto.response.GetMemberScoreResponse;
import kea.enter.enterbe.domain.member.entity.MemberState;
import kea.enter.enterbe.domain.member.repository.MemberRepository;
import kea.enter.enterbe.global.common.api.CustomResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public GetMemberScoreResponse getMemberScorePercent(Long memberId) {
        Long totalCount = memberRepository.countTotalMembers(MemberState.ACTIVE);
        Long higherCount = memberRepository.countMembersWithHigherScore(
            memberId, MemberState.ACTIVE);
        return GetMemberScoreResponse.of((double) higherCount / totalCount * 100);
    }
}
