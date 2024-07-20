package kea.enter.enterbe.domain.penalty.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "penalty")
public class Penalty extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    @Column(name = "reason", nullable = false)
    private PenaltyReason reason;
    @Column(name = "etc")
    private String etc;
    @Column(name = "level", nullable = false)
    private PenaltyLevel level;
    @Column(name = "state", nullable = false)
    private PenaltyState state;

    @Builder
    public Penalty(
        Member member,
        PenaltyReason reason, PenaltyLevel level,
        String etc,
        PenaltyState state
    ) {
        this.member = member;
        this.reason = reason;
        this.level = level;
        this.etc = etc;
        this.state = state;
    }

    public static Penalty of(
        Member member,
        PenaltyReason reason, PenaltyLevel level,
        String etc,
        PenaltyState state
    ) {
        return Penalty.builder()
            .member(member)
            .reason(reason)
            .level(level)
            .etc(etc)
            .state(state)
            .build();
    }
}

