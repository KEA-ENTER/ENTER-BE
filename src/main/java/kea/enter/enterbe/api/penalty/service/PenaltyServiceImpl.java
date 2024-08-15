package kea.enter.enterbe.api.penalty.service;

import java.util.List;
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
}
