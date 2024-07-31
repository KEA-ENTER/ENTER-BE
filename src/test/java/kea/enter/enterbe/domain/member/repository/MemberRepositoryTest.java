package kea.enter.enterbe.domain.member.repository;

import kea.enter.enterbe.IntegrationTestSupport;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.entity.MemberRole;
import kea.enter.enterbe.domain.member.entity.MemberState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class MemberRepositoryTest extends IntegrationTestSupport {
    @DisplayName(value = "멤버 아이디로 멤버를 조회한다.")
    @Test
    public void findByIdAndState() {
        // given
        Member member = memberRepository.save(createMember(MemberState.ACTIVE));
        Long memberId = member.getId();

        // when
        Optional<Member> newMember = memberRepository.findByIdAndState(memberId, MemberState.ACTIVE);

        // then
        assertThat(newMember).isPresent();
    }

    @DisplayName(value = "멤버 아이디로 멤버를 조회할 때 상태가 INACTIVE면 조회되지 않는다.")
    @Test
    public void findByIdAndStateWithInactiveState() {
        // given
        Member member = memberRepository.save(createMember(MemberState.INACTIVE));
        Long memberId = member.getId();

        // when
        Optional<Member> newMember = memberRepository.findByIdAndState(memberId, MemberState.ACTIVE);

        // then
        assertThat(newMember).isEmpty();
    }

    private Member createMember(MemberState state) {
        return Member.of("1234", "name", "test@naver.com", "password", "licenseId",
            "licensePassword", true, true, 1, MemberRole.USER, state);
    }
}