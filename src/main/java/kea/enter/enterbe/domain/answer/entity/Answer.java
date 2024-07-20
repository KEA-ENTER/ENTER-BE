package kea.enter.enterbe.domain.answer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.question.entity.Question;
import kea.enter.enterbe.global.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "answer")
public class Answer extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Member member;

    @Column(name = "answer", nullable = false)
    private String answer;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private AnswerState state;

    @Builder
    public Answer(Question question, Member member, String answer, AnswerState state) {
        this.question = question;
        this.member = member;
        this.answer = answer;
        this.state = state;
    }

    public static Answer of(Question question, Member member, String answer, AnswerState state) {
        return Answer.builder()
            .question(question)
            .member(member)
            .answer(answer)
            .state(state)
            .build();
    }
}
