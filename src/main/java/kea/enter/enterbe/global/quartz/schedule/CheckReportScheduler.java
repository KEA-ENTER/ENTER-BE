package kea.enter.enterbe.global.quartz.schedule;

import jakarta.annotation.PostConstruct;
import kea.enter.enterbe.global.algorithm.ProcessingLottery;
import kea.enter.enterbe.global.quartz.job.CheckReportJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CheckReportScheduler {
    private final SchedulerFactoryBean schedulerFactoryBean;

    @PostConstruct
    public void init() throws SchedulerException {

        JobDetail job = JobBuilder.newJob(CheckReportJob.class)
            .withIdentity("CheckReportJob", "Report")
            .withDescription("인수 반납 보고서 제출을 확인한다.")
            .build();

        Trigger trigger = TriggerBuilder.newTrigger()
            .withIdentity("CheckReportTrigger", "Report")
            .startNow()
            .withSchedule(CronScheduleBuilder.cronSchedule("0 0 10 * * ?"))
            .build();

        schedulerFactoryBean.getScheduler().scheduleJob(job, trigger);
    }
}
