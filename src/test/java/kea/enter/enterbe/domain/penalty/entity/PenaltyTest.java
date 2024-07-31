package kea.enter.enterbe.domain.penalty.entity;

import kea.enter.enterbe.IntegrationTestSupport;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.entity.MemberRole;
import kea.enter.enterbe.domain.member.entity.MemberState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PenaltyTest extends IntegrationTestSupport {
    @DisplayName("사용자 페널티를 생성한다.")
    @Test
    void create() {
        //given
        Member member = memberRepository.save(createMember());

        //when
        Penalty penalty = Penalty.create(member, PenaltyReason.BROKEN, PenaltyLevel.BLACKLIST, null);

        //then
        assertThat(penalty)
            .extracting("reason", "level", "state")
            .containsExactlyInAnyOrder(
                PenaltyReason.BROKEN, PenaltyLevel.BLACKLIST, PenaltyState.ACTIVE
            );

    }

    private Member createMember() {
        return Member.of("1234", "name", "test@naver.com", "password", "licenseId",
            "licensePassword", true, true, 1, MemberRole.USER, MemberState.ACTIVE);
    }
}