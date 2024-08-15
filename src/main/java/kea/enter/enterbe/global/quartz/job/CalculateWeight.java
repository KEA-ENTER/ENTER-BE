package kea.enter.enterbe.global.quartz.job;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import kea.enter.enterbe.api.lottery.service.dto.WeightDto;
import kea.enter.enterbe.domain.lottery.repository.WinningRepository;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.entity.MemberState;
import kea.enter.enterbe.domain.member.entity.ScoreHistory;
import kea.enter.enterbe.domain.member.entity.ScoreHistoryState;
import kea.enter.enterbe.domain.member.repository.MemberRepository;
import kea.enter.enterbe.domain.member.repository.ScoreHistoryRepository;
import kea.enter.enterbe.global.algorithm.CalculateDto;
import kea.enter.enterbe.global.algorithm.CalculateMemberDto;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CalculateWeight implements Job {

    private final MemberRepository memberRepository;
    private final WinningRepository winningRepository;
    private final ScoreHistoryRepository scoreHistoryRepository;

    @Override
    public void execute(JobExecutionContext context) {
        calculateYearsScore();
        calculateHistoryScore();
        licenseValidCheck();
    }

    public CalculateDto calculateWeight() {
        List<CalculateMemberDto> finalList = Stream.concat(
            calculateYearsScore().stream(),
            calculateHistoryScore().stream()
        ).toList();

        return CalculateDto.of(finalList);

    }

    public void licenseValidCheck() {
        List<Member> memberList = memberRepository.findByIsLicenseValidAndState(Boolean.TRUE, MemberState.ACTIVE);
        List<Member> newMemberList = new ArrayList<>();
        for (Member member : memberList) {
            member.setIsLicenseValid(Boolean.FALSE);
            newMemberList.add(member);
        }
        memberRepository.saveAll(newMemberList);
    }

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


    public List<CalculateMemberDto> calculateHistoryScore() {
        // 당첨 내역이 있는 멤버를 당첨 횟수와 함께 가져온다.

        List<Member> members = new ArrayList<>();
        List<WeightDto> weightDtoList = getApplyMemberList();
        List<ScoreHistory> histories = new ArrayList<>();
        List<CalculateMemberDto> calculateMemberDtos = new ArrayList<>();
        for (WeightDto weightDto : weightDtoList) {
            Member member = memberRepository.findByIdAndState(weightDto.getMemberId(), MemberState.ACTIVE)
                .orElseThrow(() -> new CustomException(ResponseCode.MEMBER_NOT_FOUND));
            Integer weight = calculateHistoryWeight(weightDto.getWeight());
            member.setScore(member.getScore() + weight);
            members.add(member);
            if (!weight.equals(0)) {
                histories.add(ScoreHistory.of(member, member.getScore(), String.format("당첨 횟수 가중치 계산 %d", calculateHistoryWeight(weightDto.getWeight())), ScoreHistoryState.ACTIVE));
                calculateMemberDtos.add(CalculateMemberDto.of(member.getId(), member.getName(), member.getEmail(), String.format("당첨 횟수 가중치 계산 +%d", calculateHistoryWeight(weightDto.getWeight())), member.getScore() - calculateHistoryWeight(weightDto.getWeight()), member.getScore()));
            }
        }
        memberRepository.saveAll(members);
        scoreHistoryRepository.saveAll(histories);
        return calculateMemberDtos;
    }

    public List<CalculateMemberDto> calculateYearsScore() { // 근속일수로 가중치를 계산한다.
        List<Member> members = memberRepository.findMembersByState(MemberState.ACTIVE);
        List<ScoreHistory> histories = new ArrayList<>();
        List<CalculateMemberDto> calculateMemberDtos = new ArrayList<>();


        for (Member member : members) { // 전체 멤버를 조회하여 가중치를 계산한다.
            Integer weight = calculateYearsWeight(member);
            member.setScore(20 + weight);
            if (!weight.equals(0)) { // 가중치가 0이 아닐 경우
                histories.add(ScoreHistory.of(member, member.getScore(),
                    String.format("근속 일수 가중치 계산 +%d", weight),
                    ScoreHistoryState.ACTIVE));
                calculateMemberDtos.add(
                    CalculateMemberDto.of(member.getId(), member.getName(), member.getEmail(),
                        String.format("근속 일수 가중치 계산 %d", weight), 20,
                        member.getScore()));
            }
        }
        memberRepository.saveAll(members);
        scoreHistoryRepository.saveAll(histories);
        return calculateMemberDtos;
    }

    public int calculateYearsWeight(Member member) {
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

    public int calculateHistoryWeight(Long score) {
        if (score >= 1 && score <= 2) {
            return -1;
        } else if (score >= 3 && score <= 5) {
            return -3;
        } else if (score >= 6 && score <= 10) {
            return -10;
        } else {
            return -19;
        }
    }
}
