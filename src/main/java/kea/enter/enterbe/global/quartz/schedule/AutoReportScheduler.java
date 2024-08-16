package kea.enter.enterbe.global.quartz.schedule;

import jakarta.annotation.PostConstruct;
import kea.enter.enterbe.global.quartz.job.AutoReportJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AutoReportScheduler {

    private final SchedulerFactoryBean schedulerFactoryBean;

    @PostConstruct
    public void init() throws SchedulerException {

        JobDetail job = JobBuilder.newJob(AutoReportJob.class)
            .withIdentity("AutoReport", "Report")
            .withDescription("매주 다음주의 휴일을 탐색하여 신청서를 생성한다.")
            .build();

        // 가중치 계산 때 생성한다고 기억해서 매주 수요일 9시로 crone 표현식 작성
        Trigger trigger = TriggerBuilder.newTrigger()
            .withIdentity("AutoReportTrigger", "Report")
            .startNow()
            .withSchedule(CronScheduleBuilder.cronSchedule("0 0 8.5 ? * WED"))
            .build();

        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        if (!scheduler.checkExists(job.getKey())) {
            schedulerFactoryBean.getScheduler().scheduleJob(job, trigger);
        }
    }
}
