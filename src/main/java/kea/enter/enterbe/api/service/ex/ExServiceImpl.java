package kea.enter.enterbe.api.service.ex;

import kea.enter.enterbe.api.controller.ex.dto.response.ExampleResponse;
import kea.enter.enterbe.api.service.ex.dto.ExampleDto;
import kea.enter.enterbe.domain.ex.entity.ExEntity;
import kea.enter.enterbe.domain.ex.repository.ExRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExServiceImpl implements ExService{
    private final ExRepository exRepository;

    @Override
    public ExampleResponse example(ExampleDto dto) {
        Long sum = dto.getA()+ dto.getB();
        ExEntity exEntity = ExEntity.of(sum);
        return ExampleResponse.of(exRepository.save(exEntity).getSum());
    }
}
