package kea.enter.enterbe.global.quartz.schedule;

import jakarta.annotation.PostConstruct;
import kea.enter.enterbe.global.quartz.job.ProcessingLottery;
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
public class LotteryScheduler {

    private final SchedulerFactoryBean schedulerFactoryBean;

    @PostConstruct
    public void init() throws SchedulerException {
        JobDetail job = JobBuilder.newJob(ProcessingLottery.class)
            .withIdentity("processingLottery", "Lottery")
            .withDescription("최초 당첨자를 선정한다.")
            .build();

        Trigger trigger = TriggerBuilder.newTrigger()
            .withIdentity("processingLotteryTrigger", "Lottery")
            .startNow()
            .withSchedule(CronScheduleBuilder.cronSchedule("0 0 9 ? * WED"))
            .build();

        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        if (!scheduler.checkExists(job.getKey())) {
            schedulerFactoryBean.getScheduler().scheduleJob(job, trigger);
        }
    }
}
