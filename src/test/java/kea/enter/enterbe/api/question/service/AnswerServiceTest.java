package kea.enter.enterbe.api.question.service;

import kea.enter.enterbe.IntegrationTestSupport;
import kea.enter.enterbe.api.question.controller.dto.response.GetAnswerResponseDto;
import kea.enter.enterbe.api.question.service.dto.AnswerServiceDto;
import kea.enter.enterbe.api.question.service.dto.GetAnswerServiceDto;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.entity.MemberRole;
import kea.enter.enterbe.domain.member.entity.MemberState;
import kea.enter.enterbe.domain.question.entity.Answer;
import kea.enter.enterbe.domain.question.entity.AnswerState;
import kea.enter.enterbe.domain.question.entity.Question;
import kea.enter.enterbe.domain.question.entity.QuestionCategory;
import kea.enter.enterbe.domain.question.entity.QuestionState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

public class AnswerServiceTest extends IntegrationTestSupport {

    @DisplayName(value = "문의사항 답변을 저장한다")
    @Test
    @Transactional
    public void testCreateAnswer_Success() {

        // given
        String questionContentTest = "답변 테스트 문장";
        Member member = memberRepository.save(createMember());
        Question question = questionRepository.save(createQuestion(member));

        AnswerServiceDto answerServiceDto = new AnswerServiceDto(member.getId(), questionContentTest, question.getId());

        // when
        answerService.answerQuestion(answerServiceDto);

        //then
        // 답변 저장
        Optional<Answer> optionalAnswer = answerRepository.findByQuestionId(question.getId());
        assertThat(optionalAnswer).isPresent();

        Answer answer = optionalAnswer.get();
        assertThat(answer.getContent()).isEqualTo(answerServiceDto.getContent());
        assertThat(answer.getState()).isEqualTo(AnswerState.ACTIVE);

        // 답변 저장 시 QuestionState COMPLETE로 변경 되었는지
        assertThat(answer.getQuestion().getState()).isEqualTo(QuestionState.COMPLETE);
    }

    @DisplayName(value = "문의사항 세부 내용을 조회한다")
    @Test
    @Transactional
    public void testGetAnswer_Success() {

        // given
        String answerContentTest = "답변 테스트 문장";
        Member member = memberRepository.save(createMember());
        Question question = questionRepository.save(createQuestion(member));

        AnswerServiceDto answerServiceDto = new AnswerServiceDto(member.getId(), answerContentTest, question.getId());
        answerService.answerQuestion(answerServiceDto);

        // when
        GetAnswerServiceDto getAnswerServiceDto = GetAnswerServiceDto.of(question.getId());
        GetAnswerResponseDto responseDto = answerService.getDetail(getAnswerServiceDto);

        // then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getAnswerContent()).isEqualTo(answerContentTest);
        assertThat(responseDto.getQuestionContent()).isEqualTo(question.getContent());

    }

    private Member createMember() {
        return Member.of("2", "name", "test@naver.com", "password", LocalDate.of(1999,11,28), "licenseId",
            "licensePassword", true, true, 1, MemberRole.USER, MemberState.ACTIVE);
    }

    private Question createQuestion(Member member) {
        return Question.of(member, "content", QuestionCategory.USER, QuestionState.WAIT);
    }
}
