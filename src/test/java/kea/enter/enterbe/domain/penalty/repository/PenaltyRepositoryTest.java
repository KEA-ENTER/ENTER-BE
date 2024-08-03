package kea.enter.enterbe.domain.penalty.repository;

import kea.enter.enterbe.IntegrationTestSupport;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.entity.MemberRole;
import kea.enter.enterbe.domain.member.entity.MemberState;
import kea.enter.enterbe.domain.penalty.entity.Penalty;
import kea.enter.enterbe.domain.penalty.entity.PenaltyState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

import static kea.enter.enterbe.domain.penalty.entity.PenaltyLevel.BLACKLIST;
import static kea.enter.enterbe.domain.penalty.entity.PenaltyReason.BROKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class PenaltyRepositoryTest extends IntegrationTestSupport {
    @DisplayName(value = "멤버 아이디로 사용자의 페널티 목록을 조회한다.")
    @Test
    public void findAllByMemberIdAndStateOrderByCreatedAt() {
        // given
        Member member = memberRepository.save(createMember(MemberState.ACTIVE));
        Long memberId = member.getId();

        Penalty penalty1 = createPenalty(member);
        Penalty penalty2 = createPenalty(member);
        Penalty penalty3 = createPenalty(member);

        penaltyRepository.saveAll(List.of(penalty1, penalty2, penalty3));

        // when
        List<Penalty> penaltyList = penaltyRepository.findAllByMemberIdAndStateOrderByCreatedAt(memberId, PenaltyState.ACTIVE);

        // then
        assertThat(penaltyList).hasSize(3)
            .extracting("member.id", "state")
            .containsExactlyInAnyOrder(
                tuple(memberId, PenaltyState.ACTIVE),
                tuple(memberId, PenaltyState.ACTIVE),
                tuple(memberId, PenaltyState.ACTIVE)
            );
    }

    @DisplayName(value = "멤버 아이디로 사용자의 페널티 목록을 조회할 때 생성시간 순서대로 오는지 확인한다.")
    @Test
    public void findAllByMemberIdAndStateOrderByCreatedAtCheckTrue() {
        // given
        Member member = memberRepository.save(createMember(MemberState.ACTIVE));
        Long memberId = member.getId();

        Penalty penalty1 = createPenalty(member);
        Penalty penalty2 = createPenalty(member);
        Penalty penalty3 = createPenalty(member);

        penaltyRepository.saveAll(List.of(penalty1, penalty2, penalty3));

        // when
        List<Penalty> penaltyList = penaltyRepository.findAllByMemberIdAndStateOrderByCreatedAt(memberId, PenaltyState.ACTIVE);

        // then
        assertThat(penaltyList).hasSize(3)
            .extracting("id")
            .containsExactly(
                penalty1.getId(),
                penalty2.getId(),
                penalty3.getId()
            );
    }

    @DisplayName(value = "다른 사용자의 멤버 아이디로 사용자의 페널티 목록을 조회하면 조회되지 않는다.")
    @Test
    public void findAllByMemberIdAndStateOrderByCreatedAtWithOtherMemberId() {
        // given
        Member member1 = memberRepository.save(createMember(MemberState.ACTIVE));
        Long member1Id = member1.getId();

        Member member2 = memberRepository.save(createMember(MemberState.ACTIVE));

        Penalty penalty1 = createPenalty(member2);
        Penalty penalty2 = createPenalty(member2);
        Penalty penalty3 = createPenalty(member2);

        penaltyRepository.saveAll(List.of(penalty1, penalty2, penalty3));

        // when
        List<Penalty> penaltyList = penaltyRepository.findAllByMemberIdAndStateOrderByCreatedAt(member1Id, PenaltyState.ACTIVE);

        // then
        assertThat(penaltyList).isEmpty();
    }

    @Transactional
    @DisplayName(value = "멤버 아이디로 사용자의 페널티 목록을 조회할 때 삭제된 페널티는 조회되지 않는다.")
    @Test
    public void findAllByMemberIdAndStateOrderByCreatedAtWithInactive() {
        // given
        Member member = memberRepository.save(createMember(MemberState.ACTIVE));
        Long memberId = member.getId();

        Penalty penalty1 = penaltyRepository.save(createPenalty(member));
        Penalty penalty2 = penaltyRepository.save(createPenalty(member));
        Penalty penalty3 = penaltyRepository.save(createPenalty(member));
        penalty2.deletePenalty();

        // when
        List<Penalty> penaltyList = penaltyRepository.findAllByMemberIdAndStateOrderByCreatedAt(memberId, PenaltyState.ACTIVE);

        // then
        assertThat(penaltyList).hasSize(2)
            .extracting("id")
            .containsExactly(
                penalty1.getId(),
                penalty3.getId()
            );
    }

    @DisplayName(value = "페널티 아이디와 멤버 아이디로 페널티를 조회한다.")
    @Test
    public void findByIdAndMemberIdAndState() {
        // given
        Member member = memberRepository.save(createMember(MemberState.ACTIVE));
        Long memberId = member.getId();

        Penalty newPenalty = penaltyRepository.save(createPenalty(member));
        Long newPenaltyId = newPenalty.getId();

        // when
        Optional<Penalty> penalty = penaltyRepository.findByIdAndMemberIdAndState(newPenaltyId, memberId, PenaltyState.ACTIVE);

        // then
        assertThat(penalty).isPresent();
    }

    @DisplayName(value = "페널티 아이디가 다를 경우 페널티가 조회되지 않는다.")
    @Test
    public void findByIdAndMemberIdAndStateWithOtherPenaltyId() {
        // given
        Member member = memberRepository.save(createMember(MemberState.ACTIVE));
        Long memberId = member.getId();

        Penalty newPenalty = penaltyRepository.save(createPenalty(member));
        Long newPenaltyId = newPenalty.getId();

        // when
        Optional<Penalty> penalty = penaltyRepository.findByIdAndMemberIdAndState(newPenaltyId + 1, memberId, PenaltyState.ACTIVE);

        // then
        assertThat(penalty).isEmpty();
    }

    @DisplayName(value = "멤버 아이디가 다를 경우 페널티가 조회되지 않는다.")
    @Test
    public void findByIdAndMemberIdAndStateWithOtherMemberId() {
        // given
        Member member1 = memberRepository.save(createMember(MemberState.ACTIVE));

        Member member2 = memberRepository.save(createMember(MemberState.ACTIVE));
        Long member2Id = member2.getId();

        Penalty newPenalty = penaltyRepository.save(createPenalty(member1));
        Long penaltyId = newPenalty.getId();

        // when
        Optional<Penalty> penalty = penaltyRepository.findByIdAndMemberIdAndState(penaltyId, member2Id, PenaltyState.ACTIVE);

        // then
        assertThat(penalty).isEmpty();
    }

    @DisplayName(value = "페널티 아이디와 멤버 아이디로 페널티를 조회한다.")
    @Test
    public void findByIdAndMemberId() {
        // given
        Member member = memberRepository.save(createMember(MemberState.ACTIVE));
        Long memberId = member.getId();

        Penalty newPenalty = penaltyRepository.save(createPenalty(member));
        Long newPenaltyId = newPenalty.getId();

        // when
        Optional<Penalty> penalty = penaltyRepository.findByIdAndMemberId(newPenaltyId, memberId);

        // then
        assertThat(penalty).isPresent();
    }

    private Member createMember(MemberState state) {
        return Member.of("1234", "name", "test@naver.com", "password", "licenseId",
            "licensePassword", true, true, 1, MemberRole.USER, state);
    }

    private Penalty createPenalty(Member member) {
        return Penalty.of(member, BROKEN, BLACKLIST, null);
    }
}