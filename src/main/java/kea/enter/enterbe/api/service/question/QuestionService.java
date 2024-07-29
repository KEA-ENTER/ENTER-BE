package kea.enter.enterbe.api.service.question;

import kea.enter.enterbe.api.controller.question.dto.request.QuestionRequestDto;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.repository.MemberRepository;
import kea.enter.enterbe.domain.question.entity.Question;
import kea.enter.enterbe.domain.question.entity.QuestionState;
import kea.enter.enterbe.domain.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Question createQuestion(QuestionRequestDto dto) {
        Member member = memberRepository.findById(dto.getMemberId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        // state는 작성시에 WAIT로 기본값 고정
        Question question = Question.of(member, dto.getContent(), dto.getCategory(), QuestionState.WAIT);

        return questionRepository.save(question);
    }
}
