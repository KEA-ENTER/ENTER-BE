package kea.enter.enterbe.api.apply.service;

import kea.enter.enterbe.api.apply.controller.dto.response.GetApplyDetailResponse;
import kea.enter.enterbe.api.apply.controller.dto.response.GetApplyResponse;
import kea.enter.enterbe.api.apply.controller.dto.response.GetApplyVehicleResponse;
import kea.enter.enterbe.api.apply.controller.dto.response.PostApplyResponse;
import kea.enter.enterbe.api.apply.service.dto.DeleteApplyDetailServiceDto;
import kea.enter.enterbe.api.apply.service.dto.GetApplyDetailServiceDto;
import kea.enter.enterbe.api.apply.service.dto.GetApplyServiceDto;
import kea.enter.enterbe.api.apply.service.dto.GetApplyVehicleServiceDto;
import kea.enter.enterbe.api.apply.service.dto.ModifyApplyDetailServiceDto;
import kea.enter.enterbe.api.apply.service.dto.PostApplyServiceDto;
import kea.enter.enterbe.api.lottery.controller.dto.response.GetLotteryResultResponse;
import kea.enter.enterbe.api.question.service.dto.AnswerServiceDto;
import kea.enter.enterbe.domain.apply.entity.Apply;
import kea.enter.enterbe.domain.apply.entity.ApplyRound;
import kea.enter.enterbe.domain.apply.entity.ApplyRoundState;
import kea.enter.enterbe.domain.apply.entity.ApplyState;
import kea.enter.enterbe.domain.apply.repository.ApplyRepository;
import kea.enter.enterbe.domain.apply.repository.ApplyRoundRepository;
import kea.enter.enterbe.domain.lottery.entity.Waiting;
import kea.enter.enterbe.domain.lottery.entity.WaitingState;
import kea.enter.enterbe.domain.lottery.entity.Winning;
import kea.enter.enterbe.domain.lottery.entity.WinningState;
import kea.enter.enterbe.domain.lottery.repository.WaitingRepository;
import kea.enter.enterbe.domain.lottery.repository.WinningRepository;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.entity.MemberState;
import kea.enter.enterbe.domain.member.repository.MemberRepository;
import kea.enter.enterbe.domain.penalty.entity.Penalty;
import kea.enter.enterbe.domain.penalty.entity.PenaltyLevel;
import kea.enter.enterbe.domain.penalty.entity.PenaltyReason;
import kea.enter.enterbe.domain.penalty.repository.PenaltyRepository;
import kea.enter.enterbe.domain.question.entity.Answer;
import kea.enter.enterbe.domain.question.entity.AnswerState;
import kea.enter.enterbe.domain.question.entity.Question;
import kea.enter.enterbe.domain.question.entity.QuestionState;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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


import static kea.enter.enterbe.global.common.api.CustomResponseCode.SUCCESS;
import static kea.enter.enterbe.global.common.exception.ResponseCode.APPLY_NOT_FOUND;
import static kea.enter.enterbe.global.common.exception.ResponseCode.APPLY_ROUND_NOT_FOUND;
import static kea.enter.enterbe.global.common.exception.ResponseCode.MEMBER_NOT_FOUND;
import static kea.enter.enterbe.global.common.exception.ResponseCode.WAITING_NOT_FOUND;
import static kea.enter.enterbe.global.common.exception.ResponseCode.WINNING_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ApplyServiceImpl implements ApplyService{
    private final ApplyRepository applyRepository;
    private final ApplyRoundRepository applyRoundRepository;
    private final MemberRepository memberRepository;
    private final WinningRepository winningRepository;
    private final WaitingRepository waitingRepository;
    private final PenaltyRepository penaltyRepository;

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
                            .applyRoundId(applyRound.getId())
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
                            .applyRoundId(applyRound.getId())
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
    public PostApplyResponse postApply(PostApplyServiceDto dto) {
        Optional<Member> memberOptional = findById(dto.getMemberId());
        if(!memberOptional.isPresent()){
            throw new CustomException(MEMBER_NOT_FOUND);
        }
        Member member = memberOptional.get();

        Integer age = member.getAge();
        // 만 26살 미만이라면 Exception
        if(age<26){
            return PostApplyResponse.of("APPLY-001", "만 26세 미만입니다.");
        }

        Optional<ApplyRound> applyOptional = findByApplyRoundId(dto.getApplyRoundId());
        if(!applyOptional.isPresent()){
            throw new CustomException(APPLY_ROUND_NOT_FOUND);
        }
        ApplyRound applyRound = applyOptional.get();

        Apply apply = Apply.of(member, applyRound,
            dto.getPurpose(), ApplyState.ACTIVE);

        applyRepository.save(apply);
        return PostApplyResponse.of("APPLY-002", "성공적으로 신청이 완료되었습니다.");
    }

    @Transactional
    public int deleteApplyDetail(DeleteApplyDetailServiceDto dto) {
        // 취소 가능 시간 확인
        LocalDateTime now = LocalDateTime.now();
        timeCheck(now);
        Long memberId = dto.getMemberId();

        // 당첨자 발표 이전 취소
        if(timeloop(now)){
            Apply apply = findByIdAndMemberId(dto.getApplyId(), memberId)
                .orElseThrow(() -> new CustomException(APPLY_NOT_FOUND));
            apply.deleteApply();
            return 1;
        }
        // 당첨자 발표 이후 취소
        else {
            // Apply 존재 여부 확인
            Apply apply = findByIdAndMemberId(dto.getApplyId(), memberId)
                .orElseThrow(() -> new CustomException(APPLY_NOT_FOUND));

            Long applyId = dto.getApplyId();

            // 기존 당첨자 혹은 기존 대기자 조회
            Winning firstWinning = findWinningByApplyId(applyId).orElse(null);
            Waiting firstWaiting = findWaitingByApplyId(applyId)
                .orElseThrow(() -> new CustomException(WAITING_NOT_FOUND));

            if (firstWinning == null) {
                // waiting 테이블에 있을 경우 -> 대기 취소
                // 대기자는 취소 패널티 x 당첨자만 적용
                firstWaiting.deleteWaiting();
                apply.deleteApply();
                return 1;
            } else {
                // 당첨자와 대기자 모두 취소
                firstWinning.deleteWinning();
                firstWaiting.deleteWaiting();

                // 다음 대기자를 찾음
                Integer waitingNo = firstWaiting.getWaitingNo();
                // 해당 대기 인원들의 ID를 대기번호를 기준으로 오름차순으로 정렬
                List<Long> idList = waitingFindIds(apply.getApplyRound().getId(), waitingNo);

                if (idList.isEmpty()) {
                    // 대기자가 없을 경우 -> Apply 삭제
                    if(penaltyTimeCheck())
                        applyPenalty(memberId);
                    apply.deleteApply();
                    return 0;
                } else {
                    // 대기자가 존재할 경우 -> 다음 대기자를 당첨으로
                    if(penaltyTimeCheck()){
                        // 신청 취소기간에는 다음 대기자 당첨 x
                        applyPenalty(memberId);
                        apply.deleteApply();
                        return 1;
                    }
                    else{
                        Long newWinnerId = idList.get(0);
                        Apply newWinnerApply = findByApplyId(newWinnerId)
                            .orElseThrow(() -> new CustomException(APPLY_NOT_FOUND));
                        winningRepository.save(Winning.of(newWinnerApply, WinningState.ACTIVE));

                        apply.deleteApply();
                    }
                    return 1;
                }
            }
        }
    }
    private void applyPenalty(Long memberId){
        Member member = findById(memberId).get();
        penaltyRepository.save(Penalty.of(member, PenaltyReason.APPLY, PenaltyLevel.MEDIUM, null));
    }
    public boolean penaltyTimeCheck(){
        LocalDateTime now = LocalDateTime.now();
        // 토요일
        LocalDate currentMonday = LocalDate.now().with(DayOfWeek.SATURDAY);
        LocalDateTime saturday = currentMonday.atTime(LocalTime.of(0, 0, 0));

        // 일요일
        LocalDate currentTuesday = currentMonday.with(DayOfWeek.SUNDAY);
        LocalDateTime sunday = currentTuesday.atTime(LocalTime.of(23, 59, 59));

        return now.isAfter(saturday) && now.isBefore(sunday);
    }
    public boolean timeloop(LocalDateTime now){
        // 이번주 월요일 00:00:00
        LocalDate currentMonday = LocalDate.now().with(DayOfWeek.WEDNESDAY);
        LocalDateTime monday= currentMonday.atTime(LocalTime.of(0, 0, 0));

        // 이번주 수요일 9:59:59
        LocalDate currentTuesday = currentMonday.with(DayOfWeek.WEDNESDAY);
        LocalDateTime tuesday= currentTuesday.atTime(LocalTime.of(23, 59, 59));

        return now.isAfter(monday) && now.isBefore(tuesday);
    }
    public void timeCheck(LocalDateTime now){
        // 이번주 수요일 0:00:00
        LocalDate currentMonday = LocalDate.now().with(DayOfWeek.WEDNESDAY);
        LocalDateTime mondayStart = currentMonday.atTime(LocalTime.of(0, 0, 0));

        // 이번주 수요일 9:59:59
        LocalDate currentTuesday = currentMonday.with(DayOfWeek.WEDNESDAY);
        LocalDateTime tuesdayEnd = currentTuesday.atTime(LocalTime.of(9, 59, 59));

        boolean isInRange = now.isAfter(mondayStart) && now.isBefore(tuesdayEnd);
        // 수정 기간이 아닐경우
        if (isInRange)
            throw new CustomException(ResponseCode.INVALID_QUESTION_STATE);
    }
    public List<Long> waitingFindIds(Long applyRoundId, Integer waitingNo){
        return waitingRepository.findApplyIdsByWaitingNoGreaterThanAndStateAndApplyRoundIdOrderByWaitingNoAsc(applyRoundId, waitingNo, WaitingState.ACTIVE);
    }
    public Integer findMaxWaitingNo(Long applyId){
        return waitingRepository.findMaxWaitingNoByStateAndApplyId(WaitingState.ACTIVE, applyId);
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
    public Optional<Winning> findWinningByApplyId(Long applyId){
        return winningRepository.findByApplyIdAndState(applyId, WinningState.ACTIVE);
    }
    public Optional<Waiting> findWaitingByApplyId(Long applyId){
        return waitingRepository.findByApplyIdAndState(applyId, WaitingState.ACTIVE);
    }

}
