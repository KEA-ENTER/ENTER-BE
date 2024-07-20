package kea.enter.enterbe.domain.score.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.global.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "score_history")
public class ScoreHistory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "score")
    private Integer score;

    @Column(name = "reason")
    private String reason;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private ScoreHistoryState state;

    @Builder
    public ScoreHistory(Member member, Integer score, String reason, ScoreHistoryState state) {
        this.member = member;
        this.score = score;
        this.reason = reason;
        this.state = state;
    }

    public static ScoreHistory of(Member member, Integer score, String reason, ScoreHistoryState state) {
        return ScoreHistory.builder().member(member).score(score).reason(reason).state(state).build();
    }
}
