package kea.enter.enterbe.api.apply.service;

import kea.enter.enterbe.api.apply.controller.response.GetApplyResponse;
import kea.enter.enterbe.api.apply.controller.response.GetApplyVehicleResponse;
import kea.enter.enterbe.api.apply.service.dto.GetApplyServiceDto;
import kea.enter.enterbe.api.apply.service.dto.GetApplyVehicleServiceDto;
import kea.enter.enterbe.domain.apply.entity.Apply;
import kea.enter.enterbe.domain.apply.entity.ApplyRound;
import kea.enter.enterbe.domain.apply.entity.ApplyRoundState;
import kea.enter.enterbe.domain.apply.entity.ApplyState;
import kea.enter.enterbe.domain.apply.repository.ApplyRepository;
import kea.enter.enterbe.domain.apply.repository.ApplyRoundRepository;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
import kea.enter.enterbe.domain.vehicle.repository.VehicleRepository;
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
import java.util.stream.Collectors;


import static kea.enter.enterbe.global.common.exception.ResponseCode.APPLY_ROUND_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ApplyServiceImpl implements ApplyService{
    private final ApplyRepository applyRepository;
    private final ApplyRoundRepository applyRoundRepository;

    // 신청 가능 날짜 조회 API
    @Transactional(readOnly = true)
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

    // 신청 가능한 차량 목록 API
    @Transactional(readOnly = true)
    public List<GetApplyVehicleResponse> getApplyVehicles(GetApplyVehicleServiceDto dto) {
        LocalDate takeDate = dto.getTakeDate();
        LocalDate returnDate = dto.getReturnDate();

        // 전달 받은 인수, 반납 날짜에 맞는 ApplyRound(신청 회차들을) 모두 가져온다.
        List<ApplyRound> applyRoundList = findAllByTakeDateAndReturnDate(takeDate, returnDate);

        if(applyRoundList.isEmpty()) {
            throw new CustomException(APPLY_ROUND_NOT_FOUND);
        }

        return applyRoundList.stream()
            .map(applyRound ->{
                // 각 ApplyRound에 종속된 Vehicle을 가져온다.
                Vehicle vehicle = applyRound.getVehicle();
                // 각 ApplyRound를 신청한 모든 Apply들을 가져온다. 몇명이 지원했는지를 알기 위해서
                List<Apply> applyList = findAllByApplyRoundId(applyRound.getId());

                //지원자 없을 경우
                    if(applyList.isEmpty()) {
                        return GetApplyVehicleResponse.builder()
                            .competition(0)
                            .model(vehicle.getModel())
                            .fuel(vehicle.getFuel())
                            .company(vehicle.getCompany())
                            .seat(vehicle.getSeats())
                            .img(vehicle.getImg())
                            .build();
                    }
                    //지원자 있을 경우
                    else{
                        int competition = applyList.size();
                        return GetApplyVehicleResponse.builder()
                            .competition(competition)
                            .model(vehicle.getModel())
                            .fuel(vehicle.getFuel())
                            .company(vehicle.getCompany())
                            .seat(vehicle.getSeats())
                            .img(vehicle.getImg())
                            .build();
                    }
                }
            )
            .collect(Collectors.toList());

    }
    public List<ApplyRound> findApplyRoundsByTakeDateBetween(LocalDate startDate, LocalDate endDate) {
        return applyRoundRepository.findAllByTakeDateBetweenAndState(startDate, endDate, ApplyRoundState.ACTIVE);
    }
    public List<ApplyRound> findAllByTakeDateAndReturnDate(LocalDate takeDate, LocalDate returnDate) {
        return applyRoundRepository.findAllByTakeDateAndReturnDateAndState(takeDate, returnDate, ApplyRoundState.ACTIVE);
    }
    public List<Apply> findAllByApplyRoundId(Long applyRoundId) {
        return applyRepository.findAllByApplyRoundIdAndState(applyRoundId, ApplyState.ACTIVE);
    }
}
