package kea.enter.enterbe.api.apply.service;

import kea.enter.enterbe.api.apply.controller.response.GetApplyResponse;
import kea.enter.enterbe.api.apply.service.dto.GetApplyServiceDto;
import kea.enter.enterbe.domain.apply.entity.ApplyRound;
import kea.enter.enterbe.domain.apply.entity.ApplyRoundState;
import kea.enter.enterbe.domain.apply.repository.ApplyRoundRepository;
import kea.enter.enterbe.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static kea.enter.enterbe.global.common.exception.ResponseCode.APPLY_ROUND_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplyServiceImpl implements ApplyService{
    private final ApplyRoundRepository applyRoundRepository;

    // 신청 가능 날짜 조회 API
    @Transactional
    public List<GetApplyResponse> getApply(GetApplyServiceDto dto) {
        // 이번 주 + 7 -> 다음 주를 구함
        LocalDate today = dto.getToday();
        LocalDate nextMonday = today.with(DayOfWeek.MONDAY).plusDays(7);
        LocalDate nextSunday = today.with(DayOfWeek.SUNDAY).plusDays(7);

        List<ApplyRound> applyRoundList = findApplyRoundsByTakeDateBetween(nextMonday, nextSunday);

        if(applyRoundList.isEmpty()) {
            throw new CustomException(APPLY_ROUND_NOT_FOUND);
        }
        // HashMap을 이용하여 중복된 key -> 중복된 인수 & 반납기간의 날짜를 제거
        Map<String, GetApplyResponse> applyList = new HashMap<>();

        for (ApplyRound applyRound : applyRoundList) {
            GetApplyResponse response = GetApplyResponse.builder()
                .applyRound(applyRound.getApplyRound())
                .takeDate(applyRound.getTakeDate())
                .returnDate(applyRound.getReturnDate())
                .build();

            // takeDate와 returnDate를 한쌍의 key로 설정
            String key = response.getTakeDate().toString() + response.getReturnDate().toString();

            // 중복이 제거된 신청회차정보만 저장
            applyList.putIfAbsent(key, response);
        }

        return new ArrayList<>(applyList.values());

    }


    public List<ApplyRound> findApplyRoundsByTakeDateBetween(LocalDate startDate, LocalDate endDate) {
        return applyRoundRepository.findAllByTakeDateBetweenAndState(startDate, endDate, ApplyRoundState.ACTIVE);
    }
}
