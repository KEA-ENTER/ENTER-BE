package kea.enter.enterbe.global.algorithm;

import kea.enter.enterbe.domain.member.entity.Member;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InitialLottery {

    public static Map<Member, Double> calculateScorePercentages(List<Member> members) { // 가중치 Map
        int totalScore = members.stream()
            .mapToInt(Member::getScore)
            .sum();

        return members.stream()
            .collect(Collectors.toMap(
                member -> member,
                member -> totalScore > 0 ? (double) member.getScore() / totalScore : 0
            ));
    }

    public Member processing(Map<Member, Double> map) {
        double winningNum = 0.765;
        double accumulatedWeight = 0.0;
        boolean first = true;

        while (true) {
            for (Map.Entry<Member, Double> entry : map.entrySet()) {
                accumulatedWeight += entry.getValue();
                if (accumulatedWeight >= winningNum) {
                    return entry.getKey();
                }
            }

            if (first) {
                first = false;
            } else {
                accumulatedWeight = 0.0;
            }

        }
    }
}