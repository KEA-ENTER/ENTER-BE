package kea.enter.enterbe.api.question.service;

import kea.enter.enterbe.api.question.controller.dto.request.QuestionRequestDto;
import kea.enter.enterbe.api.question.service.dto.DeleteQuestionServiceDto;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.entity.MemberState;
import kea.enter.enterbe.domain.member.repository.MemberRepository;
import kea.enter.enterbe.domain.question.entity.Question;
import kea.enter.enterbe.domain.question.entity.QuestionState;
import kea.enter.enterbe.domain.question.repository.QuestionRepository;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;

    /* 문의사항 작성 API */
    @Transactional
    public void createQuestion(QuestionRequestDto dto) {
        // memberId로 멤버 존재 여부를 검사한다.
        Member member = memberRepository.findByIdAndState(dto.getMemberId(), MemberState.ACTIVE)
            .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_MEMBER));

        // state는 작성시에 WAIT로 기본값 고정
        Question question = Question.of(member, dto.getContent(), dto.getCategory(),
            QuestionState.WAIT);
        questionRepository.save(question);

    }

    /* 문의사항 삭제 API */
    @Transactional
    public void deleteQuestion(DeleteQuestionServiceDto dto) {
        // memberId로 멤버 존재 여부를 검사한다.
        Member member = memberRepository.findByIdAndState(dto.getMemberId(), MemberState.ACTIVE)
            .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_MEMBER));

        // questionId로 문의사항 존재 여부를 검사한다.
        Question question = questionRepository.findByIdAndMemberId(dto.getQuestionId(),
                member.getId())
            .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_QUESTION));

        // 문의사항 삭제
        question.deleteQuestion();

    }
}
