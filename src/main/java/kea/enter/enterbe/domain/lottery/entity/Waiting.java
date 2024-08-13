package kea.enter.enterbe.domain.lottery.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kea.enter.enterbe.domain.apply.entity.Apply;
import kea.enter.enterbe.global.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "waiting")
public class Waiting extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apply_id", nullable = false)
    private Apply apply;

    @Column(name = "waiting_no")
    private Integer waitingNo;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private WaitingState state;

    @Builder
    public Waiting(Apply apply, int waitingNo) {
        this.apply = apply;
        this.waitingNo = waitingNo;
    }

    public static Waiting of(Apply apply, int waitingNo) {
        return Waiting.builder().apply(apply).waitingNo(waitingNo).build();
    }
    public void deleteWaiting() {
        this.state = WaitingState.INACTIVE;
    }
}
