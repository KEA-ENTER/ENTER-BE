package kea.enter.enterbe.domain.answer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    @OneToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question questionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Member authorId;

    @Column(name = "answer", nullable = false)
    private String answer;

    @Column(name = "state", nullable = false)
    private AnswerState state;

    @Builder
    public Answer(Question questionId, Member authorId, String answer, AnswerState state) {
        this.questionId = questionId;
        this.authorId = authorId;
        this.answer = answer;
        this.state = state;
    }

    public static Answer of(Question questionId, Member authorId, String answer, AnswerState state) {
        return Answer.builder()
            .questionId(questionId)
            .authorId(authorId)
            .answer(answer)
            .state(state)
            .build();
    }
}
