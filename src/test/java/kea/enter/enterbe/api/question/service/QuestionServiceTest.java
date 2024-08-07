package kea.enter.enterbe.api.question.service;

import kea.enter.enterbe.IntegrationTestSupport;
import kea.enter.enterbe.api.question.controller.dto.request.QuestionRequestDto;
import kea.enter.enterbe.api.question.service.dto.CreateQuestionServiceDto;
import kea.enter.enterbe.api.question.service.dto.DeleteQuestionServiceDto;
import kea.enter.enterbe.api.question.service.dto.ModifyQuestionServiceDto;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.entity.MemberRole;
import kea.enter.enterbe.domain.member.entity.MemberState;
import kea.enter.enterbe.domain.question.entity.Question;
import kea.enter.enterbe.domain.question.entity.QuestionCategory;
import kea.enter.enterbe.domain.question.entity.QuestionState;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class QuestionServiceTest extends IntegrationTestSupport {

    @DisplayName(value = "문의사항을 저장한다")
    @Test
    public void testCreateQuestion_Success() {
        String questionContentTest = "문의사항 테스트 문장";
        Member member = createMember();
        memberRepository.save(member);

        // given
        CreateQuestionServiceDto requestDto = new CreateQuestionServiceDto(member.getId(), questionContentTest,
            QuestionCategory.USER);

        // when
        questionService.createQuestion(requestDto);

        //then
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(1)
            .extracting("member.id", "content", "category")
            .containsExactlyInAnyOrder(
                tuple(member.getId(), questionContentTest, QuestionCategory.USER)
            );
    }

    @DisplayName(value = "존재하는 멤버 ID인지 검사한다")
    @Test
    public void testCreateQuestion_MemberNotFound() {
        Long memberIdTest = 1L;
        String questionContentTest = "문의사항 테스트 문장";
        Member member = createMember();
        memberRepository.save(member);

        // given
        CreateQuestionServiceDto requestDto = new CreateQuestionServiceDto(memberIdTest, questionContentTest,
            QuestionCategory.USER);

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> {
            questionService.createQuestion(requestDto);
        });

        assertThat(exception.getResponseCode()).isEqualTo(ResponseCode.NOT_FOUND_MEMBER);
    }

    @DisplayName(value = "문의사항을 삭제한다.")
    @Test
    public void deleteQuestion() {
        //given
        Member member = memberRepository.save(createMember());
        Long memberId = member.getId();

        Question question = questionRepository.save(createQuestion(member));
        Long questionId = question.getId();

        DeleteQuestionServiceDto dto = DeleteQuestionServiceDto.of(memberId, questionId);

        //when
        questionService.deleteQuestion(dto);

        //then
        Optional<Question> result = questionRepository.findByIdAndMemberId(questionId, memberId);
        assertThat(result).isPresent();

        assertThat(result.get())
            .extracting("state")
            .isEqualTo(QuestionState.INACTIVE);
    }

    @DisplayName(value = "존재하는 문의사항 ID인지 검사한다")
    @Test
    public void questionNotFound() {
        // given
        Member member = memberRepository.save(createMember());
        Long memberId = member.getId();
        Long testQuestionId = 999L;

        questionRepository.save(createQuestion(member));

        DeleteQuestionServiceDto dto = DeleteQuestionServiceDto.of(memberId, testQuestionId);

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> {
            questionService.deleteQuestion(dto);
        });

        assertThat(exception.getResponseCode()).isEqualTo(ResponseCode.NOT_FOUND_QUESTION);
    }

    @DisplayName(value = "문의사항 내용을 수정한다.")
    @Test
    public void modifyQuestionContent() {
        //given
        String modifyContentText = "수정된 내용입니다.";

        Member member = memberRepository.save(createMember());
        Long memberId = member.getId();

        Question question = questionRepository.save(createQuestion(member));
        Long questionId = question.getId();

        //when
        // 수정 될 content가 들어있는 requestDto
        ModifyQuestionServiceDto dto = ModifyQuestionServiceDto.of(memberId, questionId,
            modifyContentText, QuestionCategory.USER);
        questionService.modifyQuestion(dto);

        //then
        Optional<Question> result = questionRepository.findByIdAndMemberId(questionId, memberId);
        assertThat(result).isPresent();

        assertThat(result.get())
            .extracting("content")
            .isEqualTo(modifyContentText);
    }

    @DisplayName(value = "문의사항 카테고리를 수정한다.")
    @Test
    public void modifyQuestionCategory() {
        //given
        QuestionCategory modifyCategory = QuestionCategory.SERVICE;

        Member member = memberRepository.save(createMember());
        Long memberId = member.getId();

        Question question = questionRepository.save(createQuestion(member));
        Long questionId = question.getId();

        //when
        // 수정 될 category가 들어있는 requestDto
        ModifyQuestionServiceDto dto = ModifyQuestionServiceDto.of(memberId, questionId,
            question.getContent(), modifyCategory);
        questionService.modifyQuestion(dto);

        //then
        Optional<Question> result = questionRepository.findByIdAndMemberId(questionId, memberId);
        assertThat(result).isPresent();

        assertThat(result.get())
            .extracting("category")
            .isEqualTo(modifyCategory);
    }

    @DisplayName(value = "수정 가능한 문의사항인지 검사한다.")
    @Test
    public void isModifyQuestion() {

        //given
        // state가 WAIT가 아닌 값을 넣음으로써 예외 발생 테스트.
        QuestionState wrongState = QuestionState.COMPLETE;

        Member member = memberRepository.save(createMember());
        Long memberId = member.getId();

        Question question = questionRepository.save(
            Question.of(member, "content", QuestionCategory.USER, wrongState));
        Long questionId = question.getId();

        // 수정 될 내용물이 들어있는 requestDto
        ModifyQuestionServiceDto dto = ModifyQuestionServiceDto.of(memberId, questionId,
            question.getContent(), question.getCategory());

        //when & then
        CustomException exception = assertThrows(CustomException.class, () -> {
            questionService.modifyQuestion(dto);
        });

        assertThat(exception.getResponseCode()).isEqualTo(ResponseCode.INVALID_QUESTION_STATE);
    }

    private Member createMember() {
        return Member.of("2", "name", "test@naver.com", "password", LocalDate.of(1999,11,28), "licenseId",
            "licensePassword", true, true, 1, MemberRole.USER, MemberState.ACTIVE);
    }

    private Question createQuestion(Member member) {
        return Question.of(member, "content", QuestionCategory.USER, QuestionState.WAIT);
    }
}
