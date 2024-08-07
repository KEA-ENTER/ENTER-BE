package kea.enter.enterbe.api.question.service;

import kea.enter.enterbe.api.question.controller.dto.request.AnswerRequestDto;
import kea.enter.enterbe.api.question.controller.dto.response.GetAnswerResponseDto;
import kea.enter.enterbe.api.question.service.dto.AnswerServiceDto;
import kea.enter.enterbe.api.question.service.dto.EmailServiceDto;
import kea.enter.enterbe.api.question.service.dto.GetAnswerServiceDto;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.entity.MemberState;
import kea.enter.enterbe.domain.member.repository.MemberRepository;
import kea.enter.enterbe.domain.question.entity.Answer;
import kea.enter.enterbe.domain.question.entity.AnswerState;
import kea.enter.enterbe.domain.question.entity.Question;
import kea.enter.enterbe.domain.question.entity.QuestionState;
import kea.enter.enterbe.domain.question.repository.AnswerRepository;
import kea.enter.enterbe.domain.question.repository.QuestionRepository;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final AnswerRepository answerRepository;
    private final EmailService emailService;

    /* 문의 사항 답변 작성 API */
    @Transactional
    public void answerQuestion(AnswerServiceDto dto) {
        // memberId로 멤버 존재 여부를 검사한다.
        Member member = getActiveMemberById(dto.getMemberId());

        Question question = getFindByIdAndStateNot(dto.getQuestionId());
        if (question.getState() == QuestionState.COMPLETE) {
            throw new CustomException(ResponseCode.INVALID_QUESTION_STATE_COMPLETE);
        }

        // 답변을 작성했으므로 AnswerState는 ACTIVE로 고정값
        Answer answer = Answer.of(question, member, dto.getContent(), AnswerState.ACTIVE);

        // 문의사항의 state를 COMPLETE로 업데이트
        question.completeQuestion();

        answerRepository.save(answer);

        // 이메일 전송 로직 추가
        sendAnswerEmail(question, dto.getContent());
    }

    /* 답변 조회 API */
    @Transactional
    public GetAnswerResponseDto getAnswer(GetAnswerServiceDto dto) {

        // questionId로 문의사항 존재 여부를 검사한다.
        Question question = getFindByIdAndStateNot(dto.getQuestionId());

        // 답변 반환
        Optional<Answer> optionalAnswer = answerRepository.findByQuestionId(dto.getQuestionId());
        // 답변이 없을 시 null로 반환
        String content = optionalAnswer.map(Answer::getContent).orElse(null);
        LocalDateTime createdAt = optionalAnswer.map(Answer::getCreatedAt).orElse(null);

        return GetAnswerResponseDto.of(content, createdAt);
    }

    private void sendAnswerEmail(Question question, String answerContent) {
        // 수신자 Email
        String recipient = question.getMember().getEmail();
        String questionContent = question.getContent();
        String title = "[TALCAR] 문의 사항에 대한 답변 드립니다.";

        EmailServiceDto emailDto = EmailServiceDto.builder()
            .recipient(recipient)
            .title(title)
            .questionContent(questionContent)
            .answerContent(answerContent)
            .build();
        emailService.sendEmail(emailDto);
    }

    // memberId로 멤버의 존재 여부와 상태를 검사하는 메소드
    private Member getActiveMemberById(Long memberId) {
        return memberRepository.findByIdAndState(memberId, MemberState.ACTIVE)
            .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_MEMBER));
    }

    // questionId로 문의사항의 상태 확인. 삭제된 문의인 경우 에러발생
    private Question getFindByIdAndStateNot(Long questionId) {
        return questionRepository.findByIdAndStateNot(questionId, QuestionState.INACTIVE)
            .orElseThrow(() -> new CustomException(ResponseCode.INVALID_QUESTION_STATE_DELETE));
    }

}
