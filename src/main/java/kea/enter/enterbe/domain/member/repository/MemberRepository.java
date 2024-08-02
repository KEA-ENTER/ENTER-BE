package kea.enter.enterbe.domain.member.repository;

import java.util.List;
import java.util.Optional;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.entity.MemberState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByIdAndState(Long id, MemberState state);
    Optional<Member> findMemberByEmail(String email);
    Optional<List<Member>> findAllByState(MemberState state);
}

