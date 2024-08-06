package kea.enter.enterbe.global.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import kea.enter.enterbe.domain.apply.entity.Apply;
import kea.enter.enterbe.domain.apply.entity.ApplyRound;
import kea.enter.enterbe.domain.apply.entity.ApplyState;
import kea.enter.enterbe.domain.apply.repository.ApplyRepository;
import kea.enter.enterbe.domain.apply.repository.ApplyRoundRepository;
import kea.enter.enterbe.domain.lottery.entity.Waiting;
import kea.enter.enterbe.domain.lottery.entity.Winning;
import kea.enter.enterbe.domain.lottery.entity.WinningState;
import kea.enter.enterbe.domain.lottery.repository.WaitingRepository;
import kea.enter.enterbe.domain.lottery.repository.WinningRepository;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProcessingLottery {

    private final ApplyRepository applyRepository;
    private final ApplyRoundRepository applyRoundRepository;
    private final WinningRepository winningRepository;
    private final WaitingRepository waitingRepository;
    private final Random random = new Random();

    public void processingInitialLottery(long applyRoundId, int winnerCount) {
        // 회차에 참여한 회원 목록을 조회한다
        List<Member> memberLists = getApplyMembers(applyRoundId);
        // 회원 목록을 점수로 변환한다
        List<ScoreDto> scoreList = getScore(memberLists);
        // 점수를 백분율로 변환한다
        List<PercentageMembersDto> percentageMembers = transformToPercentage(scoreList);
        // 당첨자를 선정한다
        List<WinnerDto> winnerLists = processingLottery(percentageMembers, winnerCount);
        // 회차를 조회한다
        ApplyRound applyRound = getApplyRound(applyRoundId);
        // 당첨자와 대기자 데이터를 저장할 리스트
        List<Waiting> waitingList = new ArrayList<>();
        List<Winning> winningList = new ArrayList<>();
        // 당첨자 ID 목록을 추출한다
        List<Long> winnerMemberIds = winnerLists.stream()
            .map(WinnerDto::getMemberId)
            .collect(Collectors.toList());
        // 당첨자들의 신청 목록을 한 번에 조회한다
        List<Apply> applies = applyRepository.findByApplyRoundAndMemberIdInAndState(applyRound, winnerMemberIds, ApplyState.ACTIVE);
        // 신청 목록을 memberId로 매핑하여 조회 속도를 높인다
        Map<Long, Apply> applyMap = applies.stream()
            .collect(Collectors.toMap(apply -> apply.getMember().getId(), apply -> apply));
        for (WinnerDto winnerDto : winnerLists) {
            Apply applier = applyMap.get(winnerDto.getMemberId());
            if (applier == null) {
                throw new CustomException(ResponseCode.APPLY_NOT_FOUND);
            }
            Vehicle vehicle = applier.getVehicle();
            if (winnerDto.getRank() == 0) { // 최초 당첨자
                winningList.add(Winning.of(vehicle, applier, WinningState.ACTIVE));
            } else { // 대기자
                waitingList.add(Waiting.of(vehicle, applier, winnerDto.getRank()));
            }
        }
        // 당첨자와 대기자를 저장한다
        winningRepository.saveAll(winningList);
        waitingRepository.saveAll(waitingList);
    }


    private List<Member> getApplyMembers(long applyRoundId) {
        ApplyRound applyRound = applyRoundRepository.findById(applyRoundId).orElseThrow(
            () -> new CustomException(ResponseCode.APPLY_ROUND_NOT_FOUND));

        return applyRepository.findMembersBydApplyRoundAndState(applyRound);
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
            // 이 부분에서 100을 조정해 더 많은 추첨을 돌릴 수 있음.
            return PercentageMembersDto.builder()
                .memberId(scoreDto.getMemberId())
                .percentage(percentage)
                .build();
        }).collect(Collectors.toList());
    }
    private List<WinnerDto> processingLottery(List<PercentageMembersDto> percentageMembers, int winnerCount) {
        long pivot = random.nextLong() + 1;
        long initialScore = 0;
        int rank = 0;

        List<WinnerDto> winnerLists = new ArrayList<>();

        boolean foundWinners = false; // 반복 순회를 위하여, 우승자를 단 한 번의 순회로 찾을 수 있는지 확인

        while (!foundWinners) {
            foundWinners = true; // 이번 순회에서 우승자를 찾았는지 확인
            for (PercentageMembersDto percentageMember : percentageMembers) {
                initialScore += percentageMember.getPercentage();
                if (initialScore >= pivot && winnerLists.size() < winnerCount) {
                    WinnerDto winnerDto = WinnerDto.of(percentageMember.getMemberId(), rank);
                    winnerLists.add(winnerDto);
                    initialScore = 0;
                    rank++;
                }
            }
        }
        return winnerLists;
    }
}