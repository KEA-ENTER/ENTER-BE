package kea.enter.enterbe.global.algorithm;

import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.winning.entity.Winning;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Objects;

public class CalculateWeight {

    List<Winning> winningList; // 이번 반기간 당첨 내역
    List<Member> memberList; // 이번 회차 신청자 or 전체 사용자

    private void updateMemberScores(List<Member> memberList, List<Winning> winningList) {
        for (Member member : memberList) {
            int weight = calcuateWeight(member, winningList);
            int finalScore = member.getScore() + weight;
            // 여기에 jpa 코드가 들어가면 될 것 같습니다.
        }
    }


    private int calculateYears(Member members) { // 근속일수를 연으로 구하기
        LocalDateTime now = LocalDateTime.now();
        Period period = Period.between(members.getCreatedAt().toLocalDate(), now.toLocalDate());
        return period.getYears();
    }
    private int calcuateNumberOfYears(Member members) { // 근속일수 관련 가중치
        int years = calculateYears(members);
        return switch (years) {
            case 0, 1 -> 0;
            case 2 -> 1;
            case 3 -> 2;
            case 4 -> 3;
            default -> 4;
        };
    }

    private int calculateLotteryPenalty(Member member, List<Winning> winningList) { // 이전 당첨 내역 페널티, List = 반기 별 당첨내역을 가지고 와야할 듯?
        long cnt = winningList.stream()
            .filter(winning -> Objects.equals(winning.getApply().getMember().getId(), member.getId()))
            .count();
        return (cnt <= 2) ? -1 : (cnt <= 5) ? -3 : (cnt <= 10) ? -10 : -19;
    }

    private int calcuateWeight(Member members, List<Winning> winningList) { // 최종 가중치 값 리턴
        return (calculateLotteryPenalty(members, winningList) + calcuateNumberOfYears(members));
    }
}