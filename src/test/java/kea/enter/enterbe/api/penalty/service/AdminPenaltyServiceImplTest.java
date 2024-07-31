package kea.enter.enterbe.api.penalty.service;

import kea.enter.enterbe.IntegrationTestSupport;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class AdminPenaltyServiceImplTest extends IntegrationTestSupport {
    @DisplayName(value = "사용자 페널티를 생성한다")
    @Test
    public void postPenalty() throws Exception {
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

    private Member createMember() {
        return Member.of("1234", "name", "test@naver.com", "password", "licenseId",
            "licensePassword", true, true, 1, MemberRole.USER, MemberState.ACTIVE);
    }
}