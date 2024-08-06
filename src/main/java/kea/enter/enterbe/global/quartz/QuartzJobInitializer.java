package kea.enter.enterbe.global.quartz;

import kea.enter.enterbe.global.quartz.job.JobRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class QuartzJobInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final Scheduler scheduler;
    private final QuartzUtils quartzUtils;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            /*
                여기에 quartz 구현
             */
            // Scheduler 시작
            if (!scheduler.isStarted()) {
                scheduler.start();
            }
        } catch (SchedulerException e) {
            log.error("Scheduler Exception {}",e.getMessage());
        }
    }
}
