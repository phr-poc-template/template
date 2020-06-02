package jp.co.tis.phr.simulator.evh.device;

import jp.co.tis.phr.simulator.evh.Device;
import jp.co.tis.phr.simulator.evh.message.sleep.Sleep;
import jp.co.tis.phr.simulator.evh.message.sleep.SleepFormat;
import jp.co.tis.phr.simulator.evh.Simulator;
import org.quartz.SchedulerException;

/**
 * 睡眠データスキャン用デバイス（ベッドなど）
 */
public class SleepDataScanDevice extends Simulator {

    public static void main(String[] args) throws SchedulerException {
        new SleepDataScanDevice().run();
    }

    private void run() {

        Device device = new Device("Maker-A",
                "SleepDataScanDevice",
                "1.0.6",
                "w14H0001",
                "Maker-A SleepDataScanDevice");

        Sleep sleep = new Sleep();
        sleep.setState("sleep");
        sleep.setMotion(-1);
        sleep.setVitalRR(20);
        sleep.setVitalHR(54);

        SleepFormat sleepData = new SleepFormat();
        sleepData.setPatient(USER_NAME);
        sleepData.setSleep(sleep);
        sleepData.setDevice(device);

        sendData(sleepData);
    }
}
