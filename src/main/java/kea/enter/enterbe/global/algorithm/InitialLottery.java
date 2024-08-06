package kea.enter.enterbe.global.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import kea.enter.enterbe.domain.apply.entity.Apply;
import kea.enter.enterbe.domain.apply.entity.ApplyRound;
import kea.enter.enterbe.domain.apply.entity.ApplyState;
import kea.enter.enterbe.domain.apply.repository.ApplyRepository;
import kea.enter.enterbe.domain.apply.repository.ApplyRoundRepository;
import kea.enter.enterbe.domain.lottery.repository.WinningRepository;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.repository.MemberRepository;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InitialLottery {

    private final ApplyRepository applyRepository;
    private final ApplyRoundRepository applyRoundRepository;
    private final MemberRepository memberRepository;
    private final WinningRepository winningRepository;


    public List<WinnerDto> processingInitialLottery(long applyRoundId, int winnerCount) {
        List<Member> memberLists = getApplyMembers(applyRoundId); // 회차에 참여한 회원 목록을 조회한다
        List<ScoreDto> scoreList = getScore(memberLists); // 회원 목록을 점수로 변환한다
        List<PercentageMembersDto> percentageMembers = transformToPercentage(scoreList); // 점수를 백분율로 변환한다
        List<WinnerDto> winnerLists = processingLottery(percentageMembers, winnerCount); // 당첨자를 선정한다
        List<Apply> waitingList = new ArrayList<>();
        for ( WinnerDto winnerDto : winnerLists) {
            Member member = getMember(winnerDto.getMemberId());
            ApplyRound applyRound = getApplyRound(applyRoundId);
            Apply winnerApply = applyRepository.findByMemberAndApplyRoundAndState(member, applyRound, ApplyState.ACTIVE)
                .orElseThrow(() -> new CustomException(ResponseCode.APPLY_NOT_FOUND));
            Vehicle vehicle = winnerApply.getVehicle();



        }
        return processingLottery(percentageMembers, winnerCount); // 당첨자를 선정한다

    }

    private List<Member> getApplyMembers(long applyRoundId) {
        ApplyRound applyRound = applyRoundRepository.findById(applyRoundId).orElseThrow(
            () -> new CustomException(ResponseCode.APPLY_ROUND_NOT_FOUND));

        return applyRepository.findMembersBydApplyRoundAndState(applyRound);
    }

    private Member getMember(long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ResponseCode.MEMBER_NOT_FOUND));
    }

    private ApplyRound getApplyRound(long applyRoundId) {
        return applyRoundRepository.findById(applyRoundId).orElseThrow(
            () -> new CustomException(ResponseCode.APPLY_ROUND_NOT_FOUND));
    }





    private List<ScoreDto> getScore(List<Member> memberList) {
        List<ScoreDto> scoreList = new ArrayList<>();
        for (Member member : memberList) {
            ScoreDto scoreDto = ScoreDto.of(member.getId(), member.getScore());
            scoreList.add(scoreDto);
        }
        return scoreList;
    }

    public List<PercentageMembersDto> transformToPercentage(List<ScoreDto> scoreList) {
        long totalScore = scoreList.stream().mapToLong(ScoreDto::getScore).sum();
        return scoreList.stream().map(scoreDto -> {
            long percentage = (scoreDto.getScore() * 100) / totalScore;
            return PercentageMembersDto.builder()
                .memberId(scoreDto.getMemberId())
                .percentage(percentage)
                .build();
        }).collect(Collectors.toList());
    }
    private List<WinnerDto> processingLottery(List<PercentageMembersDto> percentageMembers, int winnerCount) {
        long pivot = new Random().nextLong() + 1;
        long initialScore = 0;
        int rank = 0;

        List<WinnerDto> winnerLists = new ArrayList<>();

        for (PercentageMembersDto percentageMember : percentageMembers) {
            initialScore += percentageMember.getPercentage();
            if (initialScore >= pivot && winnerLists.size() < winnerCount){
                WinnerDto winnerDto = WinnerDto.of(percentageMember.getMemberId(), rank);
                winnerLists.add(winnerDto);
                initialScore = 0;
                rank++;
            }
        }
        return winnerLists;
    }
}