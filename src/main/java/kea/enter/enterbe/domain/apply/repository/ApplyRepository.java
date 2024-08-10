package kea.enter.enterbe.domain.apply.repository;

import java.util.List;
import java.util.Optional;
import kea.enter.enterbe.domain.apply.entity.Apply;
import kea.enter.enterbe.domain.apply.entity.ApplyRound;
import kea.enter.enterbe.domain.apply.entity.ApplyRoundState;
import kea.enter.enterbe.domain.apply.entity.ApplyState;
import kea.enter.enterbe.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, Long>, ApplyCustomRepository {
    List<Apply> findAllByApplyRoundIdAndState(Long applyRoundId, ApplyState state);
    @Query("SELECT a.member.id FROM Apply a WHERE a.applyRound.id = :applyRoundId AND a.state = 'ACTIVE' ")
    List<Long> findMemberIdsByApplyRoundIdAndState(@Param("applyRoundId") Long applyRoundId); // 회차에 참여한 회원 ID 조회

    @Query("SELECT a.member FROM Apply a WHERE a.applyRound = :applyRound AND a.state = 'ACTIVE' ")
    List<Member> findMembersBydApplyRoundAndState(@Param("applyRound") ApplyRound applyRound);
    List<Apply> findByApplyRoundAndMemberIdInAndState(ApplyRound applyRound, List<Long> memberIds, ApplyState state);
    Integer countByApplyRoundRoundAndStateAndApplyRoundState(int round, ApplyState applyState, ApplyRoundState applyRoundState);
    List<Apply> findAllByMemberIdAndState(Long memberId, ApplyState state);
    List<Apply> findByApplyRoundIdInAndState(List<Long> applyRoundIds, ApplyState state);
    List<Apply> findByApplyRoundIdInAndMemberIdInAndState(List<Long> applyRoundIds, List<Long> memberIds, ApplyState state);
    Integer countByApplyRoundAndState(ApplyRound applyRound, ApplyState applyState);
    Optional<Apply> findByIdAndState(Long applyId, ApplyState applyState);
    Optional<Apply> findByIdAndMemberIdAndState(Long applyId, Long memberId, ApplyState state);

    //해당 멤버가 특정 가지고 있는 신청에서 주어진 round가 맞는 신청을 가져옴
    @Query("SELECT a FROM Apply a WHERE a.member.id = :memberId AND a.applyRound.round = :round AND a.state = :state")
    Optional<Apply> findByMemberIdAndRoundAndState(
        @Param("memberId") Long memberId,
        @Param("round") Integer round,
        @Param("state") ApplyState state
    );

    // 위의 Refactoring으로 대체됨
    //Optional<Apply> findByMemberAndApplyRoundAndState(Member member, ApplyRound applyRound, ApplyState state);

}
