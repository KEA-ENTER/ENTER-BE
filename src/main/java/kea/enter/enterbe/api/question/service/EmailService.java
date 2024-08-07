package kea.enter.enterbe.api.question.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kea.enter.enterbe.api.question.service.dto.EmailServiceDto;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendEmail(EmailServiceDto emailDto) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;

        try {
            helper = new MimeMessageHelper(message, true);
            helper.setTo(emailDto.getRecipient());
            helper.setSubject(emailDto.getTitle());
        } catch (MessagingException e) {
            throw new CustomException(ResponseCode.FAILED_MAIL_CREATE);
        }

        String body;
        try {
            body = getHtmlContent("templates/email-template.html", emailDto.getRecipient(), emailDto.getTitle(), emailDto.getQuestionContent(), emailDto.getAnswerContent());
        } catch (IOException e) {
            throw new CustomException(ResponseCode.FAILED_MAIL_TEMPLATE);
        }

        try {
            helper.setText(body, true);  // true for HTML content
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new CustomException(ResponseCode.FAILED_MAIL_SEND);
        }
    }

    private String getHtmlContent(String templatePath, String recipient, String title, String questionContent,String answerContent) throws IOException {
        ClassPathResource resource = new ClassPathResource(templatePath);
        String emailContent = new String(Files.readAllBytes(Paths.get(resource.getURI())));

        emailContent = emailContent.replace("{{questionContent}}", questionContent);
        emailContent = emailContent.replace("{{answerContent}}", answerContent);

        return emailContent;
    }
}
