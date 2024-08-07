package kea.enter.enterbe.api.question.service;

import kea.enter.enterbe.api.question.controller.dto.request.QuestionRequestDto;
import kea.enter.enterbe.api.question.service.dto.DeleteQuestionServiceDto;
import kea.enter.enterbe.api.question.service.dto.ModifyQuestionServiceDto;
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

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;

    /* 문의사항 작성 API */
    @Transactional
    public void createQuestion(QuestionRequestDto dto) {
        // memberId로 멤버 존재 여부를 검사한다.
        Member member = getActiveMemberById(dto.getMemberId());

        // state는 작성시에 WAIT로 기본값 고정
        Question question = Question.of(member, dto.getContent(), dto.getCategory(),
            QuestionState.WAIT);
        questionRepository.save(question);
    }

    /* 문의사항 삭제 API */
    @Transactional
    public void deleteQuestion(DeleteQuestionServiceDto dto) {
        // memberId로 멤버 존재 여부를 검사한다.
        Member member = getActiveMemberById(dto.getMemberId());

        // questionId로 문의사항 존재 여부를 검사한다.
        Question question = getQuestionByIdAndMemberId(dto.getQuestionId(), member.getId());

        // 문의사항 삭제
        question.deleteQuestion();
    }

    /* 문의사항 수정 API */
    @Transactional
    public void modifyQuestion(ModifyQuestionServiceDto modifyDto) {
        // memberId로 멤버 존재 여부를 검사한다.
        Member member = getActiveMemberById(modifyDto.getMemberId());

        // questionId로 문의사항 존재 여부를 검사한다.
        Question question = getQuestionByIdAndMemberId(modifyDto.getQuestionId(), member.getId());

        // 문의사항의 state를 조회하여 "WAIT" 상태가 아니면, 수정 불가하도록 한다.
        if (!question.getState().equals(QuestionState.WAIT)) {
            throw new CustomException(ResponseCode.INVALID_QUESTION_STATE);
        }

        // 문의사항 수정
        question.modifyQuestion(modifyDto.getContent(), modifyDto.getCategory(), QuestionState.WAIT);
    }

    // memberId로 멤버의 존재 여부와 상태를 검사하는 메소드
    private Member getActiveMemberById(Long memberId) {
        return memberRepository.findByIdAndState(memberId, MemberState.ACTIVE)
            .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_MEMBER));
    }

    // questionId와 memberId로 문의사항의 존재 여부를 검사하는 메소드
    private Question getQuestionByIdAndMemberId(Long questionId, Long memberId) {
        return questionRepository.findByIdAndMemberId(questionId, memberId)
            .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_QUESTION));
    }
}
