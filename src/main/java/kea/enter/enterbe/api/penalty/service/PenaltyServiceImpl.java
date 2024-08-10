package kea.enter.enterbe.api.penalty.service;

import java.util.ArrayList;
import java.util.List;
import kea.enter.enterbe.api.penalty.controller.dto.response.GetPenaltyListResponse;
import kea.enter.enterbe.api.penalty.service.dto.GetPenaltyListServiceDto;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PenaltyServiceImpl implements PenaltyService {
    private final MemberRepository memberRepository;
    private final PenaltyRepository penaltyRepository;

    public List<GetPenaltyListResponse> getPenaltyList(GetPenaltyListServiceDto dto) {
        memberRepository.findByIdAndState(dto.getMemberId(), MemberState.ACTIVE)
            .orElseThrow(() -> new CustomException(ResponseCode.MEMBER_NOT_FOUND));

        List<Penalty> penaltyList = penaltyRepository.findAllByMemberIdAndStateOrderByCreatedAt(dto.getMemberId(), PenaltyState.ACTIVE);

        List<GetPenaltyListResponse> responses = new ArrayList<>();
        String level;

        for (Penalty penalty : penaltyList) {
            level = PenaltyLevel.getDuration(penalty.getLevel());

            responses.add(GetPenaltyListResponse.builder()
                    .penaltyId(penalty.getId())
                    .reason(penalty.getReason())
                    .level(level)
                    .createdAt(penalty.getCreatedAt().toLocalDate().toString())
                .build());
        }

        return responses;
    }

}
