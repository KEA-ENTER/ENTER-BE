package kea.enter.enterbe.api.penalty.service;

import kea.enter.enterbe.IntegrationTestSupport;
import kea.enter.enterbe.api.penalty.controller.response.GetPenaltyListResponse;
import kea.enter.enterbe.api.penalty.service.dto.DeletePenaltyServiceDto;
import kea.enter.enterbe.api.penalty.service.dto.GetPenaltyListServiceDto;
import kea.enter.enterbe.api.penalty.service.dto.PostPenaltyServiceDto;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.entity.MemberRole;
import kea.enter.enterbe.domain.member.entity.MemberState;
import kea.enter.enterbe.domain.penalty.entity.Penalty;
import kea.enter.enterbe.domain.penalty.entity.PenaltyLevel;
import kea.enter.enterbe.domain.penalty.entity.PenaltyReason;
import kea.enter.enterbe.domain.penalty.entity.PenaltyState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;

import static kea.enter.enterbe.domain.penalty.entity.PenaltyLevel.BLACKLIST;
import static kea.enter.enterbe.domain.penalty.entity.PenaltyReason.BROKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class AdminPenaltyServiceImplTest extends IntegrationTestSupport {
    @DisplayName(value = "사용자 페널티를 생성한다")
    @Test
    public void postPenalty() {
        //given
        Member member = memberRepository.save(createMember());
        PostPenaltyServiceDto dto = PostPenaltyServiceDto.of(member.getId(), PenaltyReason.BROKEN, PenaltyLevel.BLACKLIST, null);

        //when
        adminPenaltyService.createPenalty(dto);

        //then
        List<Penalty> penaltyList = penaltyRepository.findAll();
        assertThat(penaltyList).hasSize(1)
            .extracting("reason", "level", "state")
            .containsExactlyInAnyOrder(
                tuple(PenaltyReason.BROKEN, PenaltyLevel.BLACKLIST, PenaltyState.ACTIVE)
            );
    }

    @DisplayName(value = "사용자의 페널티 목록을 조회한다.")
    @Test
    public void getPenaltyList() {
        //given
        Member member = memberRepository.save(createMember());
        Long memberId = member.getId();

        Penalty penalty1 = penaltyRepository.save(createPenalty(member));
        Penalty penalty2 = penaltyRepository.save(createPenalty(member));
        Penalty penalty3 = penaltyRepository.save(createPenalty(member));
        penaltyRepository.saveAll(List.of(penalty1, penalty2, penalty3));

        GetPenaltyListServiceDto dto = GetPenaltyListServiceDto.of(memberId);

        //when
        List<GetPenaltyListResponse> penaltyList = adminPenaltyService.getPenaltyList(dto);

        //then
        assertThat(penaltyList).hasSize(3)
            .extracting("reason", "level")
            .containsExactlyInAnyOrder(
                tuple(PenaltyReason.BROKEN, PenaltyLevel.BLACKLIST),
                tuple(PenaltyReason.BROKEN, PenaltyLevel.BLACKLIST),
                tuple(PenaltyReason.BROKEN, PenaltyLevel.BLACKLIST)
            );
    }

    @DisplayName(value = "사용자의 페널티를 삭제한다.")
    @Test
    public void deletePenalty() {
        //given
        Member member = memberRepository.save(createMember());
        Long memberId = member.getId();

        Penalty penalty = penaltyRepository.save(createPenalty(member));
        Long penaltyId = penalty.getId();

        DeletePenaltyServiceDto dto = DeletePenaltyServiceDto.of(memberId, penaltyId);

        //when
        adminPenaltyService.deletePenalty(dto);

        //then
        Optional<Penalty> result = penaltyRepository.findByIdAndMemberIdAndState(penaltyId, memberId, PenaltyState.ACTIVE);
        assertThat(result).isEmpty();
    }

    private Member createMember() {
        return Member.of("1234", "name", "test@naver.com", "password", "licenseId",
            "licensePassword", true, true, 1, MemberRole.USER, MemberState.ACTIVE);
    }

    private Penalty createPenalty(Member member) {
        return Penalty.of(member, BROKEN, BLACKLIST, null);
    }
}