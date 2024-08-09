package kea.enter.enterbe.global.quartz.job;

import kea.enter.enterbe.api.member.service.MemberService;
import kea.enter.enterbe.api.vehicle.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class CheckReportJob implements Job {

    private final VehicleService vehicleService;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Check Report Job");
        vehicleService.checkVehicleReport();
    }
}
