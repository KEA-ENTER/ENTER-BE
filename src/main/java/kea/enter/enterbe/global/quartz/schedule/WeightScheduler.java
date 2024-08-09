package kea.enter.enterbe.global.quartz.schedule;

import jakarta.annotation.PostConstruct;
import kea.enter.enterbe.global.quartz.job.CalculateWeight;
import lombok.RequiredArgsConstructor;
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
public class WeightScheduler{

    private final SchedulerFactoryBean schedulerFactoryBean;

    @PostConstruct
    public void init() throws SchedulerException {
        JobDetail job = JobBuilder.newJob(CalculateWeight.class)
            .withIdentity("calculateWeight", "Lottery")
            .withDescription("반기 별 당첨자를 조회하여 가중치를 계산한다.")
            .build();

        Trigger trigger = TriggerBuilder.newTrigger()
            .withIdentity("calculateWeightTrigger", "Lottery")
            .startNow()
            .withSchedule(CronScheduleBuilder.cronSchedule("0 0 8 ? * MON"))
            .build();

        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        if (!scheduler.checkExists(job.getKey())) {
            schedulerFactoryBean.getScheduler().scheduleJob(job, trigger);
        }
    }
}
