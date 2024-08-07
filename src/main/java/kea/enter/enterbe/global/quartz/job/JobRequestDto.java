package kea.enter.enterbe.global.quartz.job;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.quartz.JobDataMap;

@Getter
public class JobRequestDto {

    private String jobName;
    private String jobGroup;
    private String jobDescription;
    private JobDataMap jobDataMap;
    private String cronExpression;
    private LocalDateTime startDate;
    private long repeatIntervalInSeconds;
    private int repeatCount;
    private int retry;

    @Builder
    public JobRequestDto(String jobName, String jobGroup, String jobDescription,
        JobDataMap jobDataMap, String cronExpression, LocalDateTime startDate,
        int repeatIntervalInSeconds, int repeatCount, int retry) {
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.jobDescription = jobDescription;
        this.jobDataMap = jobDataMap;
        this.cronExpression = cronExpression;
        this.startDate = startDate;
        this.repeatIntervalInSeconds = repeatIntervalInSeconds;
        this.repeatCount = repeatCount;
        this.retry = retry;
    }

    public static JobRequestDto of(String jobName, String jobGroup, String jobDescription,
        JobDataMap jobDataMap, String cronExpression, LocalDateTime startDate,
        int repeatIntervalInSeconds, int repeatCount, int retry){
        return JobRequestDto.builder()
            .jobName(jobName)
            .jobGroup(jobGroup)
            .jobDescription(jobDescription)
            .jobDataMap(jobDataMap)
            .cronExpression(cronExpression)
            .startDate(startDate)
            .repeatIntervalInSeconds(repeatIntervalInSeconds)
            .repeatCount(repeatCount)
            .retry(retry)
            .build();
    }
}
