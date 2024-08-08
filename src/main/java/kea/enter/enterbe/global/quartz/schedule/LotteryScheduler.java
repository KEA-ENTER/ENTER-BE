package kea.enter.enterbe.global.quartz.schedule;

import jakarta.annotation.PostConstruct;
import kea.enter.enterbe.global.algorithm.ProcessingLottery;
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
public class LotteryScheduler {

    private SchedulerFactory schedulerFactory;
    private Scheduler scheduler;

    @PostConstruct
    public void init() throws SchedulerException {
        schedulerFactory = new StdSchedulerFactory();
        scheduler = schedulerFactory.getScheduler();
        scheduler.start();

        JobDetail job = JobBuilder.newJob(ProcessingLottery.class)
            .withIdentity("processingLottery", "Lottery")
            .withDescription("최초 당첨자를 선정한다.")
            .build();

        Trigger trigger = TriggerBuilder.newTrigger()
            .withIdentity("processingLotteryTrigger", "Lottery")
            .startNow()
            .withSchedule(CronScheduleBuilder.cronSchedule("0 0 9 ? * WED"))
            .build();

        scheduler.scheduleJob(job, trigger);
    }
}
