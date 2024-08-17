package kea.enter.enterbe.api.question.service;

import kea.enter.enterbe.api.question.controller.dto.response.GetQuestionDetailResponseDto;
import kea.enter.enterbe.api.question.controller.dto.response.GetQuestionListResponseDto;
import kea.enter.enterbe.api.question.service.dto.CreateQuestionServiceDto;
import kea.enter.enterbe.api.question.service.dto.DeleteQuestionServiceDto;
import kea.enter.enterbe.api.question.service.dto.GetQuestionDetailServiceDto;
import kea.enter.enterbe.api.question.service.dto.GetQuestionListServiceDto;
import kea.enter.enterbe.api.question.service.dto.ModifyQuestionServiceDto;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.entity.MemberState;
import kea.enter.enterbe.domain.member.repository.MemberRepository;
import kea.enter.enterbe.domain.question.entity.Answer;
import kea.enter.enterbe.domain.question.entity.Question;
import kea.enter.enterbe.domain.question.entity.QuestionCategory;
import kea.enter.enterbe.domain.question.entity.QuestionState;
import kea.enter.enterbe.domain.question.repository.AnswerRepository;
import kea.enter.enterbe.domain.question.repository.QuestionRepository;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final AnswerRepository answerRepository;

    /* 문의사항 작성 API */
    @Transactional
    public void createQuestion(CreateQuestionServiceDto dto) {

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

    /* 문의사항 상세 내용 조회 API (사용자) */
    @Transactional
    public GetQuestionDetailResponseDto getDetail(GetQuestionDetailServiceDto dto) {

        /* 문의사항 조회 */
        // 삭제 된 문의사항인지 확인 후 조회
        Question question = getFindByIdAndStateNot(dto.getQuestionId());
        // 작성자 이름, 컨텐트, 카테고리, 질문 날짜
        String questionMemberName = question.getMember().getName();
        String questionContent = question.getContent();
        QuestionCategory questionCategory = question.getCategory();
        LocalDateTime questionCreatedAt = question.getCreatedAt();

        boolean myQuestion;
        myQuestion = Objects.equals(dto.getMemberId(), question.getMember().getId());

        /* 답변 조회 */
        // 답변 반환
        Optional<Answer> optionalAnswer = answerRepository.findByQuestionId(dto.getQuestionId());
        // 답변이 없을 시 null로 반환
        String answerContent = optionalAnswer.map(Answer::getContent).orElse(null);
        LocalDateTime answerCreatedAt = optionalAnswer.map(Answer::getCreatedAt).orElse(null);

        return GetQuestionDetailResponseDto.of(questionMemberName, questionContent, questionCategory,
            localDateTimeToString(questionCreatedAt), answerContent, localDateTimeToString(answerCreatedAt), myQuestion);
    }

    /* 문의사항 List 조회 API (사용자) */
    @Transactional
    public GetQuestionListResponseDto getQuestionList(GetQuestionListServiceDto dto) {

        /* 문의사항 List 조회 */
        // 삭제된 질문사항 제외
        List<Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createdAt"));
        Pageable pageable = PageRequest.of(dto.getPageNumber(), 4, Sort.by(sorts));
        Page<Question> questions = questionRepository.searchQuestions(dto.getKeyword(),
            dto.getSearchType(), pageable);

        // getContent를 통해 리스트로 변환
        List<GetQuestionListResponseDto.QuestionDetailDto> questionDetailDtos = new ArrayList<>();
        for (Question question : questions.getContent()) {
            questionDetailDtos.add(
                GetQuestionListResponseDto.QuestionDetailDto.of(
                    question.getId(),
                    question.getMember().getName(),
                    question.getContent(),
                    question.getCategory(),
                    localDateTimeToString(question.getCreatedAt()),
                    question.getState()
                )
            );
        }

        return GetQuestionListResponseDto.of(
            questionDetailDtos,
            questions.getTotalPages(),
            questions.hasNext()
        );

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

    public String localDateTimeToString(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        Date date = java.sql.Timestamp.valueOf(localDateTime);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    // questionId로 문의사항의 상태 확인. 삭제된 문의인 경우 에러발생
    private Question getFindByIdAndStateNot(Long questionId) {
        // 문의사항이 있는지 확인
        questionRepository.findById(questionId)
            .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_QUESTION));
        return questionRepository.findByIdAndStateNot(questionId, QuestionState.INACTIVE)
            .orElseThrow(() -> new CustomException(ResponseCode.INVALID_QUESTION_STATE_DELETE));
    }
}
