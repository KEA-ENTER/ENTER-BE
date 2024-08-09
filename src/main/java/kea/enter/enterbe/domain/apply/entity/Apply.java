package kea.enter.enterbe.domain.apply.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.question.entity.QuestionCategory;
import kea.enter.enterbe.domain.question.entity.QuestionState;
import kea.enter.enterbe.global.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "apply")
public class Apply extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="apply_round_id", nullable = false)
    private ApplyRound applyRound;

    @Enumerated(EnumType.STRING)
    @Column(name="purpose", nullable = false)
    private ApplyPurpose purpose;

    @Enumerated(EnumType.STRING)
    @Column(name="state", nullable = false)
    private ApplyState state;


    @Builder
    public Apply(Member member, ApplyRound applyRound, ApplyPurpose purpose, ApplyState state){
        this.member = member;
        this.applyRound = applyRound;
        this.purpose = purpose;
        this.state = state;
    }

    public static Apply of(Member member, ApplyRound applyRound, ApplyPurpose purpose, ApplyState state){
        return Apply.builder()
            .member(member)
            .applyRound(applyRound)
            .purpose(purpose)
            .state(state)
            .build();
    }

    public void modifyApplyRound(ApplyRound applyRound, ApplyPurpose purpose) {
        this.applyRound = applyRound;
        this.purpose = purpose;
    }
}
