package kea.enter.enterbe.domain.member.entity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findMemberByEmail(String email);

}
