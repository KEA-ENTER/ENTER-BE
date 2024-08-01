package kea.enter.enterbe.api.question.service;

import kea.enter.enterbe.IntegrationTestSupport;
import kea.enter.enterbe.api.question.controller.dto.request.QuestionRequestDto;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.entity.MemberRole;
import kea.enter.enterbe.domain.member.entity.MemberState;
import kea.enter.enterbe.domain.question.entity.Question;
import kea.enter.enterbe.domain.question.entity.QuestionCategory;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;

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
        QuestionRequestDto requestDto = new QuestionRequestDto(member.getId(), questionContentTest,
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
        QuestionRequestDto requestDto = new QuestionRequestDto(memberIdTest, questionContentTest,
            QuestionCategory.USER);

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> {
            questionService.createQuestion(requestDto);
        });

        assertThat(exception.getResponseCode()).isEqualTo(ResponseCode.NOT_FOUND_MEMBER);
    }

    private Member createMember() {
        return Member.of("2", "name", "test@naver.com", "password", "licenseId",
            "licensePassword", true, true, 1, MemberRole.USER, MemberState.ACTIVE);
    }
}
