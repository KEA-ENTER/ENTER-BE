package kea.enter.enterbe.domain.member.repository;

import java.util.List;
import java.util.Optional;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.entity.MemberState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByIdAndState(Long id, MemberState state);

    Optional<Member> findMemberByEmailAndState(String email, MemberState state);

    Optional<List<Member>> findAllByState(MemberState state);

    @Query("SELECT COUNT(m) "
        + "FROM Member m "
        + "WHERE m.score > (SELECT m2.score FROM Member m2 WHERE m2.id = :memberId AND m2.state = :state)")
    Long countMembersWithHigherScore(
        @Param("memberId") Long memberId,
        @Param("state") MemberState state);

    @Query("SELECT COUNT(m) FROM Member m WHERE m.state = :state")
    Long countTotalMembers(@Param("state") MemberState state);
}

