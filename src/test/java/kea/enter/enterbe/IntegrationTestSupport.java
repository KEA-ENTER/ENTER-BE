package kea.enter.enterbe;

import kea.enter.enterbe.api.service.ex.ExService;
import kea.enter.enterbe.api.question.service.QuestionService;
import kea.enter.enterbe.domain.ex.repository.ExRepository;
import kea.enter.enterbe.domain.member.repository.MemberRepository;
import kea.enter.enterbe.domain.question.repository.QuestionRepository;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class IntegrationTestSupport {

    @Autowired
    protected ExService exService;

    @Autowired
    protected QuestionService questionService;

    @Autowired
    protected ExRepository exRepository;

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected QuestionRepository questionRepository;

    @AfterEach
    void tearDown() {
        exRepository.deleteAllInBatch();
        questionRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }
}
