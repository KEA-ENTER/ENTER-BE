package kea.enter.enterbe.domain.apply.repository;

import java.util.List;
import java.util.Optional;
import kea.enter.enterbe.domain.apply.entity.Apply;
import kea.enter.enterbe.domain.apply.entity.ApplyRound;
import kea.enter.enterbe.domain.apply.entity.ApplyState;
import kea.enter.enterbe.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, Long> {
    List<Apply> findAllByApplyRoundIdAndState(Long applyRoundId, ApplyState state);

    @Query("SELECT a.member.id FROM Apply a WHERE a.applyRound.id = :applyRoundId AND a.state = 'ACTIVE' ")
    List<Long> findMemberIdsByApplyRoundIdAndState(@Param("applyRoundId") Long applyRoundId); // 회차에 참여한 회원 ID 조회

    @Query("SELECT a.member FROM Apply a WHERE a.applyRound = :applyRound AND a.state = 'ACTIVE' ")
    List<Member> findMembersBydApplyRoundAndState(@Param("applyRound") ApplyRound applyRound);

    Optional<Apply> findByMemberAndApplyRoundAndState(Member member, ApplyRound applyRound, ApplyState state);

}
