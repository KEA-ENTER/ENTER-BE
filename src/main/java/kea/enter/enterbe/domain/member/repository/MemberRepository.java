package kea.enter.enterbe.domain.member.repository;

import java.util.Optional;
import kea.enter.enterbe.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findById(
        @Param("memberId") Long memberId
    );
}
