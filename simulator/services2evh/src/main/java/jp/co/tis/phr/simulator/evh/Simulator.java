package jp.co.tis.phr.simulator.evh;

import com.microsoft.azure.eventhubs.EventHubException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.util.TimeZone;

import static jp.co.tis.phr.simulator.evh.EventHubsClient.sendMessage;

@Slf4j
public class Simulator implements Job {

    protected static final String USER_NAME
            = StringUtils.isNotEmpty(System.getenv("USER_NAME"))
            ? System.getenv("USER_NAME") : "demo01";

    private final String CRON_EXPRESSION
            = StringUtils.isNotEmpty(System.getenv("CRON_EXPRESSION"))
            ? System.getenv("CRON_EXPRESSION") : null;

    public static final String JOB_DATA = "jobData";

    protected Scheduler scheduler;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        JobDataMap data = jobExecutionContext.getJobDetail().getJobDataMap();
        EventHubsMessage dm = (EventHubsMessage) data.get(JOB_DATA);
        try {
            sendMessage(dm);
        } catch (IOException|EventHubException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T extends EventHubsMessage> void sendData(T deviceMessage) {

        // env["CRON_EXPRESSION"]が設定されていないときは1回だけ起動する
        if (CRON_EXPRESSION == null) {
            try {
                sendMessage(deviceMessage);
            } catch (IOException | EventHubException e) {
                throw new RuntimeException(e);
            }
            System.exit(0);
        }

        // スケジュールで動かしたいとき
        try {
            scheduleJob(deviceMessage, CRON_EXPRESSION);
            scheduler.start();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T extends EventHubsMessage> void scheduleJob(T deviceMessage, String cronExpression) throws SchedulerException {

        scheduler = StdSchedulerFactory.getDefaultScheduler();

        JobDataMap map = new JobDataMap();
        map.put(JOB_DATA, deviceMessage);
        final String jobGroup = RandomStringUtils.randomAscii(5);
        JobDetail job = JobBuilder.newJob(this.getClass())
                .withIdentity("job-" + jobGroup, jobGroup)
                .usingJobData(map)
                .storeDurably()
                .build();
        scheduler.addJob(job, false);
        CronTrigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("trigger-" + jobGroup, jobGroup)
                .withSchedule(
                        CronScheduleBuilder.cronSchedule(cronExpression)
                                .inTimeZone(TimeZone.getTimeZone("Asia/Tokyo")))
                .forJob(job).startNow().build();
        scheduler.scheduleJob(trigger);
    }

}
