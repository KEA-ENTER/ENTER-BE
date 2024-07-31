package kea.enter.enterbe.api.penalty.service;

import kea.enter.enterbe.api.penalty.service.dto.PostPenaltyServiceDto;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.entity.MemberState;
import kea.enter.enterbe.domain.member.repository.MemberRepository;
import kea.enter.enterbe.domain.penalty.entity.Penalty;
import kea.enter.enterbe.domain.penalty.repository.PenaltyRepository;
import kea.enter.enterbe.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static kea.enter.enterbe.global.common.exception.ResponseCode.MEMBER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminPenaltyServiceImpl implements AdminPenaltyService {
    private final PenaltyRepository penaltyRepository;
    private final MemberRepository memberRepository;

    /* 페널티 부여 API */
    @Transactional
    public void createPenalty(PostPenaltyServiceDto service) {
        // MemberId로 멤버 존재 여부를 검사하고 페널티를 부여한다
        Member member = findMemberById(service.getMemberId());
        penaltyRepository.save(Penalty.of(member, service.getReason(), service.getLevel(), service.getEtc()));
    }

    public Member findMemberById(Long memberId) {
        return memberRepository.findByIdAndState(memberId, MemberState.ACTIVE)
            .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
    }
}
