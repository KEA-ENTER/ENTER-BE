package kea.enter.enterbe;

import kea.enter.enterbe.api.service.ex.ExService;
import kea.enter.enterbe.domain.ex.repository.ExRepository;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class IntegrationTestSupport {

    @Autowired
    protected ExService exService;
    @Autowired
    protected ExRepository exRepository;

    @AfterEach
    void tearDown() {
        exRepository.deleteAllInBatch();
    }
}
