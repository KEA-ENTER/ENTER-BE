package kea.enter.enterbe.api.apply.service;

import kea.enter.enterbe.api.apply.controller.dto.response.GetApplyDetailResponse;
import kea.enter.enterbe.api.apply.controller.dto.response.GetApplyResponse;
import kea.enter.enterbe.api.apply.controller.dto.response.GetApplyVehicleResponse;
import kea.enter.enterbe.api.apply.service.dto.DeleteApplyDetailServiceDto;
import kea.enter.enterbe.api.apply.service.dto.GetApplyDetailServiceDto;
import kea.enter.enterbe.api.apply.service.dto.GetApplyServiceDto;
import kea.enter.enterbe.api.apply.service.dto.GetApplyVehicleServiceDto;
import kea.enter.enterbe.api.apply.service.dto.ModifyApplyDetailServiceDto;
import kea.enter.enterbe.api.apply.service.dto.PostApplyServiceDto;
import kea.enter.enterbe.api.question.service.dto.AnswerServiceDto;
import kea.enter.enterbe.domain.apply.entity.Apply;
import kea.enter.enterbe.domain.apply.entity.ApplyRound;
import kea.enter.enterbe.domain.apply.entity.ApplyRoundState;
import kea.enter.enterbe.domain.apply.entity.ApplyState;
import kea.enter.enterbe.domain.apply.repository.ApplyRepository;
import kea.enter.enterbe.domain.apply.repository.ApplyRoundRepository;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.entity.MemberState;
import kea.enter.enterbe.domain.member.repository.MemberRepository;
import kea.enter.enterbe.domain.question.entity.Answer;
import kea.enter.enterbe.domain.question.entity.AnswerState;
import kea.enter.enterbe.domain.question.entity.Question;
import kea.enter.enterbe.domain.question.entity.QuestionState;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
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
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.*;


import static kea.enter.enterbe.global.common.exception.ResponseCode.APPLY_NOT_FOUND;
import static kea.enter.enterbe.global.common.exception.ResponseCode.APPLY_ROUND_NOT_FOUND;
import static kea.enter.enterbe.global.common.exception.ResponseCode.MEMBER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ApplyServiceImpl implements ApplyService{
    private final ApplyRepository applyRepository;
    private final ApplyRoundRepository applyRoundRepository;
    private final MemberRepository memberRepository;

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
                .round(applyRound.getRound())
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
                            .vehicleId(vehicle.getId())
                            .round(applyRound.getRound())
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
                            .vehicleId(vehicle.getId())
                            .round(applyRound.getRound())
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
    // 차량 신청 내역 조회 API
    @Transactional(readOnly = true)
    public GetApplyDetailResponse getApplyDetail(GetApplyDetailServiceDto dto) {
        Long memberId = dto.getMemberId();
        int maxRound = getMaxRoundByState();

        Optional<Apply> applyOptional= findByMemberIdAndRound(memberId, maxRound);
        Apply recentlyApply = applyOptional.orElseThrow(() -> new CustomException(APPLY_NOT_FOUND));


        // 최신 신청 목록의 신청 회차 정보와 차량 정보를 가져온다.
        ApplyRound applyRound = recentlyApply.getApplyRound();
        Vehicle vehicle = recentlyApply.getApplyRound().getVehicle();
        int competition = countByApplyRound(applyRound);

        return GetApplyDetailResponse.builder()
            .applyId(recentlyApply.getId())
            .takeDate(recentlyApply.getApplyRound().getTakeDate())
            .competition(competition)
            .purpose(recentlyApply.getPurpose())
            .model(vehicle.getModel())
            .fuel(vehicle.getFuel())
            .company(vehicle.getCompany())
            .seat(vehicle.getSeats())
            .img(vehicle.getImg())
            .build();
    }

    @Transactional
    public void modifyApplyDetail(ModifyApplyDetailServiceDto dto) {
        //수정 가능 시간 확인
        timeCheck(LocalDateTime.now());

        // 새로 선택한 ApplyRound로 수정
        Optional<Apply> applyOptional = findByApplyId(dto.getApplyId());
        Optional<ApplyRound> applyRoundOptional = findByApplyRoundId(dto.getApplyRoundId());
        if (!applyOptional.isPresent()) {
            throw new CustomException(APPLY_NOT_FOUND);
        }
        if (!applyRoundOptional.isPresent()) {
            throw new CustomException(APPLY_ROUND_NOT_FOUND);
        }
        Apply apply = applyOptional.get();
        ApplyRound applyRound = applyRoundOptional.get();
        apply.modifyApplyRound(applyRound, dto.getPurpose());

    }
    @Transactional
    public void postApply(PostApplyServiceDto dto) {
        Optional<Member> memberOptional = findById(dto.getMemberId());
        if(!memberOptional.isPresent()){
            throw new CustomException(MEMBER_NOT_FOUND);
        }
        Member member = memberOptional.get();

        Integer age = member.getAge();
        // 만 26살 미만이라면 Exception
        if(age<26){
            throw new CustomException(ResponseCode.AGE_NOT_ALLOWED);
        }

        Optional<ApplyRound> applyOptional = findByApplyRoundId(dto.getApplyRoundId());
        if(!applyOptional.isPresent()){
            throw new CustomException(APPLY_ROUND_NOT_FOUND);
        }
        ApplyRound applyRound = applyOptional.get();

        Apply apply = Apply.of(member, applyRound,
            dto.getPurpose(), ApplyState.ACTIVE);

        applyRepository.save(apply);
    }

    @Transactional
    public void deleteApplyDetail(DeleteApplyDetailServiceDto dto) {
        //수정 가능 시간 확인
        timeCheck(LocalDateTime.now());

        // Apply 존재 여부 확인
        Optional<Apply> applyOptional = findByIdAndMemberId(dto.getApplyId(), dto.getMemberId());
        if (!applyOptional.isPresent()) {
            throw new CustomException(APPLY_NOT_FOUND);
        }

        Apply apply = applyOptional.get();
        apply.deleteApply();

    }
    public void timeCheck(LocalDateTime now){
        // 이번주 수요일 0:00:00
        LocalDate currentMonday = LocalDate.now().with(DayOfWeek.WEDNESDAY);
        LocalDateTime mondayStart = currentMonday.atTime(LocalTime.of(0, 0, 0));

        // 이번주 수요일 9:59:59
        LocalDate nextTuesday = currentMonday.with(DayOfWeek.WEDNESDAY);
        LocalDateTime tuesdayEnd = nextTuesday.atTime(LocalTime.of(9, 59, 59));

        boolean isInRange = now.isAfter(mondayStart) && now.isBefore(tuesdayEnd);
        // 수정 기간이 아닐경우
        if (isInRange)
            throw new CustomException(ResponseCode.INVALID_QUESTION_STATE);
    }
    public Optional<Member> findById(Long memberId){
        return memberRepository.findByIdAndState(memberId, MemberState.ACTIVE);
    }

    public Optional<Apply> findByMemberIdAndRound(Long memberId, int maxRound) {
        return applyRepository.findByMemberIdAndRoundAndState(memberId, maxRound, ApplyState.ACTIVE);
    }
    public Integer getMaxRoundByState() {
        return applyRoundRepository.findMaxRoundByState(ApplyRoundState.ACTIVE);
    }
    public  Optional<Apply>findByIdAndMemberId(Long applyId, Long memberId){
        return applyRepository.findByIdAndMemberIdAndState(applyId, memberId, ApplyState.ACTIVE);
    }
    public Optional<ApplyRound> findByApplyRoundId(Long applyRoundId){
        return applyRoundRepository.findByIdAndState(applyRoundId, ApplyRoundState.ACTIVE);
    }
    public Optional<Apply> findByApplyId(Long applyId){
        return applyRepository.findByIdAndState(applyId, ApplyState.ACTIVE);
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
    public Integer countByApplyRound(ApplyRound applyRound){
        return applyRepository.countByApplyRoundAndState(applyRound, ApplyState.ACTIVE);
    }

}
