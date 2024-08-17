package kea.enter.enterbe.global.quartz.job;

import kea.enter.enterbe.domain.apply.entity.ApplyRound;
import kea.enter.enterbe.domain.apply.entity.ApplyRoundState;
import kea.enter.enterbe.domain.apply.repository.ApplyRoundRepository;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import kea.enter.enterbe.domain.vehicle.repository.VehicleRepository;
import kea.enter.enterbe.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static kea.enter.enterbe.global.common.exception.ResponseCode.APPLY_NOT_FOUND;
import static kea.enter.enterbe.global.common.exception.ResponseCode.FILE_NOT_FOUND;
import static kea.enter.enterbe.global.common.exception.ResponseCode.HOLIDAY_NOT_FOUND;
import static kea.enter.enterbe.global.common.exception.ResponseCode.VEHICLE_NOT_VALID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AutoReportJob implements Job {

    private final VehicleRepository vehicleRepository;
    private final ApplyRoundRepository applyRoundRepository;

    @Override
    public void execute(JobExecutionContext context) {
        List<String[]> nextWeekHolidays = getNextWeekHolidays();
        System.out.println(nextWeekHolidays);
        createApplyRounds(nextWeekHolidays);
    }

    public void createApplyRounds(List<String[]> holidayPairs) {
        if (holidayPairs.isEmpty()) {
            // 휴일을 못찾을 경우
            throw new CustomException(HOLIDAY_NOT_FOUND);
        }
        int round;
        if (applyRoundRepository.countAllApplyRounds() == 0)
            //round가 아무것도 없을 때
            round = 0;
        else
            round = getMaxRoundByState();// 현재 회차 찾기

        // 현재 string form yyyyMMdd
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        //각 요일쌍으로 실행
        for (String[] holidayPair : holidayPairs) {
            if (holidayPair == null) {
                throw new CustomException(HOLIDAY_NOT_FOUND);
            }
            LocalDate takeDay = LocalDate.parse(holidayPair[0], formatter);
            LocalDate returnDay = LocalDate.parse(holidayPair[1], formatter);


            // 사용할 Vehicle 선택 (여기선 첫 번째 Vehicle을 선택)
            List<Vehicle> vehicleList = getAvailableOrOnRentVehicles();

            if (vehicleList.isEmpty())
                throw new CustomException(VEHICLE_NOT_VALID);

            //각 차량마다 현재 실행중인 요일에 맞게 저장
            for (Vehicle vehicle : vehicleList){
                ApplyRound applyRound = ApplyRound.of(
                    vehicle,
                    (round+1),
                    takeDay,
                    returnDay,
                    ApplyRoundState.ACTIVE
                );
                applyRoundRepository.save(applyRound);
            }

        }
    }

    public List<String[]> getNextWeekHolidays() {
        List<String[]> holidayPairs = new ArrayList<>();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("data/EventDays.txt");
        if (inputStream == null) {
            throw new CustomException(FILE_NOT_FOUND);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            LocalDate today = LocalDate.now();
            LocalDate nextMonday = today.with(DayOfWeek.MONDAY).plusWeeks(1);
            LocalDate nextSunday = nextMonday.with(DayOfWeek.SUNDAY);

            // 현재 string form yyyyMMdd
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

            holidayPairs = reader.lines()
                .map(String::trim)
                .map(line -> line.split(" - "))  //  " - " 기준으로 날짜 분리
                .filter(dates -> {
                    LocalDate startDate = LocalDate.parse(dates[0], formatter);
                    //endDate는 휴일이 연속일 경우 대여 가능한 주에서 다음주로 넘어갈 수 있기 때문에
                    //startDate를 기준으로 날짜를 탐색
                    return (startDate.isAfter(nextMonday.minusDays(1)) && startDate.isBefore(nextSunday.plusDays(1)));
                })
                .toList(); // 한쌍의 날짜 리스트


        } catch (IOException e) {
            e.printStackTrace();
        }

        return holidayPairs;
    }

    // 현재 대여중 혹은 사용가능한 차량을 가져옴
    public List<Vehicle> getAvailableOrOnRentVehicles() {
        List<VehicleState> states = Arrays.asList(VehicleState.AVAILABLE, VehicleState.ON_RENT);
        return vehicleRepository.findByStateIn(states);
    }
    // 현재 회차를 가져옴
    public Integer getMaxRoundByState() {
        return applyRoundRepository.findMaxRoundByState(ApplyRoundState.ACTIVE);
    }


}