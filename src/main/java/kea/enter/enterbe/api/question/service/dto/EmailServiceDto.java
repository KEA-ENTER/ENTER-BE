package kea.enter.enterbe.api.question.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmailServiceDto {
    private String recipient;
    private String title;
    private String questionContent;
    private String answerContent;

    @Builder
    public EmailServiceDto(String recipient, String title, String questionContent, String answerContent) {
        this.recipient = recipient;
        this.title = title;
        this.questionContent = questionContent;
        this.answerContent = answerContent;
    }

    public static EmailServiceDto of(String recipient, String title, String questionContent, String answerContent) {
        return EmailServiceDto.builder()
            .recipient(recipient)
            .title(title)
            .questionContent(questionContent)
            .answerContent(answerContent)
            .build();
    }
}
