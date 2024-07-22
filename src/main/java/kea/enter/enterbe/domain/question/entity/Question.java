package kea.enter.enterbe.domain.question.entity;

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
@Table(name = "question")
public class Question extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Member member;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private QuestionCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private QuestionState state;

    @Builder
    public Question(Member member, String title, String content, QuestionCategory category,
        QuestionState state) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.category = category;
        this.state = state;
    }

    public static Question of(Member member, String title, String content, QuestionCategory category,
        QuestionState state) {
        return Question.builder()
            .member(member)
            .title(title)
            .content(content)
            .category(category)
            .state(state)
            .build();
    }

}
