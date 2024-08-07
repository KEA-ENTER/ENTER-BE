package kea.enter.enterbe.api.penalty.service;

import kea.enter.enterbe.api.penalty.controller.dto.response.GetAdminPenaltyListResponse;
import kea.enter.enterbe.api.penalty.service.dto.DeleteAdminPenaltyServiceDto;
import kea.enter.enterbe.api.penalty.service.dto.GetAdminPenaltyListServiceDto;
import kea.enter.enterbe.api.penalty.service.dto.PostAdminPenaltyServiceDto;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.entity.MemberState;
import kea.enter.enterbe.domain.member.repository.MemberRepository;
import kea.enter.enterbe.domain.penalty.entity.Penalty;
import kea.enter.enterbe.domain.penalty.entity.PenaltyState;
import kea.enter.enterbe.domain.penalty.repository.PenaltyRepository;
import kea.enter.enterbe.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static kea.enter.enterbe.global.common.exception.ResponseCode.MEMBER_NOT_FOUND;
import static kea.enter.enterbe.global.common.exception.ResponseCode.PENALTY_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminPenaltyServiceImpl implements AdminPenaltyService {
    private final PenaltyRepository penaltyRepository;
    private final MemberRepository memberRepository;

    /* 페널티 부여 API */
    @Transactional
    public void createPenalty(PostAdminPenaltyServiceDto dto) {
        // memberId로 멤버 존재 여부를 검사하고 페널티를 부여한다
        Member member = findMemberById(dto.getMemberId());
        penaltyRepository.save(Penalty.of(member, dto.getReason(), dto.getLevel(), dto.getEtc()));
    }

    /* 페널티 목록 조회 API */
    public List<GetAdminPenaltyListResponse> getPenaltyList(GetAdminPenaltyListServiceDto dto) {
        // memberId로 멤버 존재 여부를 검사하고 페널티를 부여한다
        Member member = findMemberById(dto.getMemberId());

        // 해당 사용자의 페널티 내역을 조회한다
        List<Penalty> penaltyList = penaltyRepository.findAllByMemberIdAndStateOrderByCreatedAt(member.getId(), PenaltyState.ACTIVE);
        return penaltyList.stream()
            .map(d -> GetAdminPenaltyListResponse.builder()
                .penaltyId(d.getId())
                .createdAt(localDateTimeToString(d.getCreatedAt()))
                .reason(d.getReason())
                .level(d.getLevel())
                .etc(d.getEtc())
                .build()).toList();
    }

    /* 페널티 삭제 API */
    @Transactional
    public void deletePenalty(DeleteAdminPenaltyServiceDto dto) {
        // memberId로 멤버 존재 여부를 검사하고 페널티를 부여한다
        Member member = findMemberById(dto.getMemberId());

        // penaltyId로 페널티 존재 여부를 검사하고 페널티를 삭제한다
        Penalty penalty = findPenaltyByIdAndMemberId(dto.getPenaltyId(), member.getId());
        penalty.deletePenalty();
    }

    // 멤버 조회
    public Member findMemberById(Long memberId) {
        return memberRepository.findByIdAndState(memberId, MemberState.ACTIVE)
            .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
    }

    // 멤버 페널티 조회
    public Penalty findPenaltyByIdAndMemberId(Long penaltyId, Long memberId) {
        return penaltyRepository.findByIdAndMemberIdAndState(penaltyId, memberId, PenaltyState.ACTIVE)
            .orElseThrow(() -> new CustomException(PENALTY_NOT_FOUND));
    }

    public String localDateTimeToString(LocalDateTime localDateTime) {
        Date date = java.sql.Timestamp.valueOf(localDateTime);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
}
