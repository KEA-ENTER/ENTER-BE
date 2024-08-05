package kea.enter.enterbe.api.lottery.service;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kea.enter.enterbe.api.lottery.dto.WeightDto;
import kea.enter.enterbe.domain.apply.repository.ApplyRepository;
import kea.enter.enterbe.domain.lottery.repository.WinningRepository;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.entity.MemberState;
import kea.enter.enterbe.domain.member.repository.MemberRepository;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalculateWeight {

    private final MemberRepository memberRepository;
    private final WinningRepository winningRepository;
    private final ApplyRepository applyRepository;

    public List<WeightDto> getApplyMemberList() { // 반기 별 당첨자를 조회한다.
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate;
        LocalDateTime endDate;

        if (now.getMonthValue() <= 6) {
            startDate = LocalDateTime.of(now.getYear(), Month.JANUARY, 1, 0, 0);
            endDate = LocalDateTime.of(now.getYear(), Month.JUNE, 30, 23, 59, 59);
        } else {
            startDate = LocalDateTime.of(now.getYear(), Month.JULY, 1, 0, 0);
            endDate = LocalDateTime.of(now.getYear(), Month.DECEMBER, 31, 23, 59, 59);
        }

        return winningRepository.countActiveEntitiesByHalfYearAndState(startDate, endDate);
    }
    private void updateWeight(List<WeightDto> weightDtoList, long applyRoundId) {
        List<Member> memberList = new ArrayList<>();
        List<Long> applyMemberList = applyRepository.findMemberIdsByApplyRoundId(applyRoundId);

        Map<Long, WeightDto> weightDtoMap = weightDtoList.stream()
            .collect(Collectors.toMap(WeightDto::getMemberId, dto -> dto));

        for (Long memberId : applyMemberList) {
            if (weightDtoMap.containsKey(memberId)) {
                WeightDto weightDto = weightDtoMap.get(memberId);
                Member member = memberRepository.findByIdAndState(memberId, MemberState.ACTIVE)
                    .orElseThrow(() -> new CustomException(ResponseCode.MEMBER_NOT_FOUND));
                member.setScore(
                    20 + calculateYears(member) + calculateHistory(
                        weightDto.getWeight()));
                memberList.add(member);
            }
        }
        memberRepository.saveAll(memberList);
    }
    private int calculateYears(Member member) { // 근속일수로 가중치를 계산한다.
        LocalDateTime now = LocalDateTime.now();
        Period period = Period.between(member.getCreatedAt().toLocalDate(), now.toLocalDate());
        return switch (period.getYears()) {
            case 0, 1 -> 0;
            case 2 -> 1;
            case 3 -> 2;
            case 4 -> 3;
            default -> 4;
        };
    }

    private int calculateHistory(Long score) {
            if (score >= 1 && score <= 2) {
                return -1;
            } else if (score >= 3 && score <= 5) {
                return -3;
            } else if (score >= 6 && score <= 10) {
                return -10;
            } else return -19;
            }
        }
