package kea.enter.enterbe.api.apply.service;

import kea.enter.enterbe.api.apply.service.dto.DeleteApplyDetailServiceDto;
import kea.enter.enterbe.domain.apply.entity.Apply;
import kea.enter.enterbe.domain.apply.entity.ApplyState;
import kea.enter.enterbe.domain.apply.repository.ApplyRepository;
import kea.enter.enterbe.domain.apply.repository.ApplyRoundRepository;
import kea.enter.enterbe.domain.lottery.entity.Waiting;
import kea.enter.enterbe.domain.lottery.entity.WaitingState;
import kea.enter.enterbe.domain.lottery.entity.Winning;
import kea.enter.enterbe.domain.lottery.entity.WinningState;
import kea.enter.enterbe.domain.lottery.repository.WaitingRepository;
import kea.enter.enterbe.domain.lottery.repository.WinningRepository;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.entity.MemberState;
import kea.enter.enterbe.domain.member.repository.MemberRepository;
import kea.enter.enterbe.domain.penalty.entity.Penalty;
import kea.enter.enterbe.domain.penalty.entity.PenaltyLevel;
import kea.enter.enterbe.domain.penalty.entity.PenaltyReason;
import kea.enter.enterbe.domain.penalty.repository.PenaltyRepository;
import kea.enter.enterbe.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

import static kea.enter.enterbe.global.common.exception.ResponseCode.APPLY_NOT_FOUND;
import static kea.enter.enterbe.global.common.exception.ResponseCode.WAITING_NOT_FOUND;

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
    @Transactional
    public void deleteApplyDetailBefore(DeleteApplyDetailServiceDto dto) {
        Long memberId = dto.getMemberId();

        Apply apply = findByIdAndMemberId(dto.getApplyId(), memberId)
            .orElseThrow(() -> new CustomException(APPLY_NOT_FOUND));
        apply.deleteApply();
    }

    @Transactional
    public int deleteApplyDetailAfter(DeleteApplyDetailServiceDto dto) {
        Long memberId = dto.getMemberId();

        // Apply 존재 여부 확인
        Apply apply = findByIdAndMemberId(dto.getApplyId(), memberId)
            .orElseThrow(() -> new CustomException(APPLY_NOT_FOUND));

        Long applyId = dto.getApplyId();

        // 기존 당첨자 혹은 기존 대기자 조회
        Winning firstWinning = findWinningByApplyId(applyId).orElse(null);
        Waiting firstWaiting = findWaitingByApplyId(applyId)
            .orElseThrow(() -> new CustomException(WAITING_NOT_FOUND));

        if (firstWinning == null) {
            // waiting 테이블에 있을 경우 -> 대기 취소
            // 대기자는 취소 패널티 x 당첨자만 적용
            firstWaiting.deleteWaiting();
            apply.deleteApply();
            return 1;
        } else {
            // 당첨자와 대기자 모두 취소
            firstWinning.deleteWinning();
            firstWaiting.deleteWaiting();

            // 다음 대기자를 찾음
            Integer waitingNo = firstWaiting.getWaitingNo();
            // 해당 대기 인원들의 ID를 대기번호를 기준으로 오름차순으로 정렬
            List<Long> idList = waitingFindIds(apply.getApplyRound().getId(), waitingNo);

            if (idList.isEmpty()) {
                // 대기자가 없을 경우 -> Apply 삭제
                apply.deleteApply();
                return 0;
            } else {
                // 대기자가 존재할 경우 -> 다음 대기자를 당첨으로
                Long newWinnerId = idList.get(0);
                Apply newWinnerApply = findByApplyId(newWinnerId)
                    .orElseThrow(() -> new CustomException(APPLY_NOT_FOUND));
                winningRepository.save(Winning.of(newWinnerApply, WinningState.ACTIVE));

                apply.deleteApply();
                return 1;
            }
        }
    }

    public Optional<Apply> findByApplyId(Long applyId){
        return applyRepository.findByIdAndState(applyId, ApplyState.ACTIVE);
    }
    private void applyPenalty(Long memberId){
        Member member = findById(memberId).get();
        penaltyRepository.save(Penalty.of(member, PenaltyReason.APPLY, PenaltyLevel.MEDIUM, null));
    }
    public Optional<Member> findById(Long memberId){
        return memberRepository.findByIdAndState(memberId, MemberState.ACTIVE);
    }
    public  Optional<Apply>findByIdAndMemberId(Long applyId, Long memberId){
        return applyRepository.findByIdAndMemberIdAndState(applyId, memberId, ApplyState.ACTIVE);
    }
    public Optional<Winning> findWinningByApplyId(Long applyId){
        return winningRepository.findByApplyIdAndState(applyId, WinningState.ACTIVE);
    }
    public Optional<Waiting> findWaitingByApplyId(Long applyId){
        return waitingRepository.findByApplyIdAndState(applyId, WaitingState.ACTIVE);
    }
    public List<Long> waitingFindIds(Long applyRoundId, Integer waitingNo){
        return waitingRepository.findApplyIdsByWaitingNoGreaterThanAndStateAndApplyRoundIdOrderByWaitingNoAsc(applyRoundId, waitingNo, WaitingState.ACTIVE);
    }
}
