package kea.enter.enterbe.api.apply.service;

import kea.enter.enterbe.api.apply.service.dto.DeleteApplyDetailServiceDto;
import kea.enter.enterbe.domain.apply.repository.ApplyRepository;
import kea.enter.enterbe.domain.apply.repository.ApplyRoundRepository;
import kea.enter.enterbe.domain.lottery.repository.WaitingRepository;
import kea.enter.enterbe.domain.lottery.repository.WinningRepository;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.entity.MemberState;
import kea.enter.enterbe.domain.member.repository.MemberRepository;
import kea.enter.enterbe.domain.penalty.entity.Penalty;
import kea.enter.enterbe.domain.penalty.entity.PenaltyLevel;
import kea.enter.enterbe.domain.penalty.entity.PenaltyReason;
import kea.enter.enterbe.domain.penalty.repository.PenaltyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ApplyServiceTest implements ApplyServiceTestImpl{
    private final ApplyRepository applyRepository;
    private final ApplyRoundRepository applyRoundRepository;
    private final MemberRepository memberRepository;
    private final WinningRepository winningRepository;
    private final WaitingRepository waitingRepository;
    private final PenaltyRepository penaltyRepository;
    @Override
    public void deleteApplyDetailBefore(DeleteApplyDetailServiceDto dto) {

    }

    @Override
    public int deleteApplyDetailAfter(DeleteApplyDetailServiceDto dto) {
        return 0;
    }

    @Override
    public void deleteApplyDetailPenalty(DeleteApplyDetailServiceDto dto) {

    }


    private void applyPenalty(Long memberId){
        Member member = findById(memberId).get();
        penaltyRepository.save(Penalty.of(member, PenaltyReason.APPLY, PenaltyLevel.MEDIUM, null));
    }
    public Optional<Member> findById(Long memberId){
        return memberRepository.findByIdAndState(memberId, MemberState.ACTIVE);
    }
}
