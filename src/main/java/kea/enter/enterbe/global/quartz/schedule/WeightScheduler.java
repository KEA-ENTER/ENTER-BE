package kea.enter.enterbe.global.quartz.schedule;

import jakarta.annotation.PostConstruct;
import kea.enter.enterbe.api.lottery.service.CalculateWeight;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Component;

@Component
public class WeightScheduler{

    private SchedulerFactory schedulerFactory;
    private Scheduler scheduler;

    @PostConstruct
    public void init() throws SchedulerException {
        schedulerFactory = new StdSchedulerFactory();
        scheduler = schedulerFactory.getScheduler();
        scheduler.start();

        JobDetail job = JobBuilder.newJob(CalculateWeight.class)
            .withIdentity("calculateWeight", "Lottery")
            .withDescription("반기 별 당첨자를 조회하여 가중치를 계산한다.")
            .build();

        Trigger trigger = TriggerBuilder.newTrigger()
            .withIdentity("calculateWeightTrigger", "Lottery")
            .startNow()
            .withSchedule(CronScheduleBuilder.cronSchedule("0 0 8 ? * MON"))
            .build();

        scheduler.scheduleJob(job, trigger);
    }
}
