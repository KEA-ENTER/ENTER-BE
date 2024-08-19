package kea.enter.enterbe.api.penalty.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import kea.enter.enterbe.api.penalty.controller.dto.response.GetMemberInPenaltyPeriodResponse;
import kea.enter.enterbe.api.penalty.controller.dto.response.GetPenaltyListResponse;
import kea.enter.enterbe.api.penalty.controller.dto.response.GetPenaltyResponse;
import kea.enter.enterbe.api.penalty.controller.dto.response.PenaltyInfo;
import kea.enter.enterbe.api.penalty.service.dto.GetPenaltyListServiceDto;
import kea.enter.enterbe.api.penalty.service.dto.GetPenaltyServiceDto;
import kea.enter.enterbe.domain.member.entity.MemberState;
import kea.enter.enterbe.domain.member.repository.MemberRepository;
import kea.enter.enterbe.domain.penalty.entity.Penalty;
import kea.enter.enterbe.domain.penalty.entity.PenaltyLevel;
import kea.enter.enterbe.domain.penalty.entity.PenaltyState;
import kea.enter.enterbe.domain.penalty.repository.PenaltyRepository;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PenaltyServiceImpl implements PenaltyService {
    private final MemberRepository memberRepository;
    private final PenaltyRepository penaltyRepository;

    public GetPenaltyListResponse getPenaltyList(GetPenaltyListServiceDto dto) {
        memberRepository.findByIdAndState(dto.getMemberId(), MemberState.ACTIVE)
            .orElseThrow(() -> new CustomException(ResponseCode.MEMBER_NOT_FOUND));

        Page<Penalty> penaltyPage = penaltyRepository.findAllByMemberIdAndStateOrderByCreatedAtDesc(dto.getMemberId(), PenaltyState.ACTIVE, dto.getPageable());
        List<PenaltyInfo> penaltyInfoList = penaltyPage.stream()
            .map(d -> PenaltyInfo.of(d.getId(), d.getReason().toString(), d.getLevel().toString(), d.getCreatedAt().toLocalDate().toString()))
            .toList();

        return GetPenaltyListResponse.of(
            penaltyInfoList,
            penaltyPage.getNumber(),
            penaltyPage.getSize(),
            penaltyPage.getTotalElements(),
            penaltyPage.getTotalPages(),
            penaltyPage.hasNext()
        );
    }

    public GetPenaltyResponse getPenalty(GetPenaltyServiceDto dto) {
        memberRepository.findByIdAndState(dto.getMemberId(), MemberState.ACTIVE)
            .orElseThrow(() -> new CustomException(ResponseCode.MEMBER_NOT_FOUND));

        Penalty penalty = penaltyRepository.findByIdAndMemberIdAndState(dto.getPenaltyId(), dto.getMemberId(), PenaltyState.ACTIVE)
            .orElseThrow(() -> new CustomException(ResponseCode.PENALTY_NOT_FOUND));

        return GetPenaltyResponse.builder()
            .penaltyId(penalty.getId())
            .reason(penalty.map(penalty.getReason()))
            .level(PenaltyLevel.getDuration(penalty.getLevel()))
            .etc(penalty.getEtc())
            .createdAt(penalty.getCreatedAt().toLocalDate().toString())
            .build();
    }

    @Override
    public GetMemberInPenaltyPeriodResponse getMemberInPenaltyPeriod(Long memberId) {
        // 사용자의 패널티 내역 조회
        List<Penalty> penaltyList = penaltyRepository.findAllByMemberIdAndStateOrderByCreatedAt(memberId, PenaltyState.ACTIVE);
        // 패널티가 없는 경우
        if(penaltyList.isEmpty()){
            return GetMemberInPenaltyPeriodResponse.of(false, 0,0,0);
        }
        // 패널티가 유지되는 마지막 시간을 먼저 현재 시간으로 선언
        LocalDate penaltyEnd = LocalDate.now();
        // 현재 패널티 제한을 받는지 여부를 담는 변수, 초기값은 false
        boolean inProgress = false;

        // 패널티 생성일자를 담는 스택
        Deque<LocalDate> createdAtStack = new LinkedList<>();
        // 기간 자체에 대한 스택
        Deque<Long> periodStack = new LinkedList<>();
        // 패널티 종료일을 쌓는 스택 -> 솔직히 스택 안써도 될 것 같긴한데 그냥 변수를 쓰기가 싫어
        Deque<LocalDate> endStack = new LinkedList<>();

        // 각 패널티를 조회하며 연산
        for(Penalty penalty : penaltyList) {
            // 블랙리스트를 보게 되면 연산 종료 및 return 할 수 있도록 진행 (뭔가 변수가 필요할 듯)
            if(penalty.getLevel().equals(PenaltyLevel.BLACKLIST)){
                return GetMemberInPenaltyPeriodResponse.of(true, 9999, 12,30);
            }

            LocalDate createdAt = penalty.getCreatedAt().toLocalDate();
            createdAtStack.push(createdAt);
            Long period = penaltyConvertToPeriod(penalty.getLevel());
            periodStack.push(period);
            // 이전 createdAtStack과 비교했을 때 넘어선다면 이전 것을 기준으로 패널티 덧샘 시작
            if(createdAtStack.size()>=2 && !endStack.isEmpty() && endStack.peek().isAfter(createdAtStack.peek())){
                createdAtStack.pop();
                LocalDate periodOfEnd = createdAtStack.peek();
                while(!periodStack.isEmpty()){
                    periodOfEnd = periodOfEnd.plusWeeks(periodStack.pop());
                }
                endStack.push(periodOfEnd);
            }else {
                LocalDate periodOfEnd = createdAtStack.peek().plusWeeks(periodStack.peek());
                endStack.push(periodOfEnd);
            }


            // 현재 값보다 넘어가는 경우를 확인
            if(endStack.peek().isAfter(penaltyEnd) || endStack.peek().isEqual(penaltyEnd)){
                penaltyEnd = endStack.peek();
                inProgress = true;
            }

        }
        Period restOfPenalties = Period.between(LocalDate.now(), penaltyEnd);
        return GetMemberInPenaltyPeriodResponse.of(inProgress, restOfPenalties.getYears(), restOfPenalties.getMonths(), restOfPenalties.getDays());
    }

    private Long penaltyConvertToPeriod(PenaltyLevel level){
        return switch (level) {
            case MINIMUM -> 2L;
            case LOW -> 4L;
            case MEDIUM ->16L;
            case HIGH -> 64L;
            case BLACKLIST -> 99999L;
        };
    }
}
