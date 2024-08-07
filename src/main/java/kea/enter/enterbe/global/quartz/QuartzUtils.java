package kea.enter.enterbe.global.quartz;

import static org.quartz.CronExpression.isValidExpression;

import java.time.ZoneId;
import java.util.Date;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import kea.enter.enterbe.global.quartz.job.JobRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class QuartzUtils {

    public static JobDetail createJob(JobRequestDto jobRequestDto, Class<? extends Job> jobClass,
        ApplicationContext context) {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(jobClass);
        factoryBean.setDurability(false);
        factoryBean.setApplicationContext(context);
        factoryBean.setName(jobRequestDto.getJobName());
        factoryBean.setGroup(jobRequestDto.getJobGroup());
        factoryBean.setDescription(jobRequestDto.getJobDescription());
        if (jobRequestDto.getJobDataMap() != null) {
            factoryBean.setJobDataMap(jobRequestDto.getJobDataMap());
        }

        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }

    public static Trigger createTrigger(JobRequestDto jobRequestDto) {
        String cronExpression = jobRequestDto.getCronExpression();
        if (!isValidExpression(cronExpression)) {
            throw new CustomException(ResponseCode.INVALID_QUARTZ_EXPRESSION_TRIGGER);
        } else {
            return createSimpleTrigger(jobRequestDto);
        }
    }


    private static Trigger createSimpleTrigger(JobRequestDto jobRequestDto) {
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setName(jobRequestDto.getJobName());
        factoryBean.setGroup(jobRequestDto.getJobGroup());
        factoryBean.setStartTime(
            Date.from(jobRequestDto.getStartDate().atZone(ZoneId.systemDefault()).toInstant()));
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        factoryBean.setRepeatInterval(jobRequestDto.getRepeatIntervalInSeconds() * 1000); //ms 단위임
        factoryBean.setRepeatCount(jobRequestDto.getRepeatCount());

        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }
}

