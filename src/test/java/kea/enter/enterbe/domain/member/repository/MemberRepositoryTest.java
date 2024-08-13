package kea.enter.enterbe.domain.member.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Optional;
import kea.enter.enterbe.IntegrationTestSupport;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.entity.MemberRole;
import kea.enter.enterbe.domain.member.entity.MemberState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberRepositoryTest extends IntegrationTestSupport {
    @DisplayName(value = "멤버 아이디로 멤버를 조회한다. (성공)")
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

    @DisplayName(value = "멤버 아이디로 멤버를 조회한다. (실패: 비활성화 상태)")
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
        return Member.of("name", "test@naver.com", "password", LocalDate.of(1999,11,28), "licenseId",
            "licensePassword", true, true, 1, MemberRole.USER, state);
    }
}