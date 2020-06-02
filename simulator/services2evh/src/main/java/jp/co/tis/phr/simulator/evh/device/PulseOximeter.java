package jp.co.tis.phr.simulator.evh.device;

import jp.co.tis.phr.simulator.evh.Device;
import jp.co.tis.phr.simulator.evh.message.vital.Vital;
import jp.co.tis.phr.simulator.evh.message.vital.VitalFormat;
import jp.co.tis.phr.simulator.evh.message.vital.Vitals;
import jp.co.tis.phr.simulator.evh.Simulator;
import org.quartz.SchedulerException;

/**
 * パルスオキシメーター
 */
public class PulseOximeter extends Simulator {

    public static void main(String[] args) throws SchedulerException {
        new PulseOximeter().run();
    }

    private void run() {

        Device device = new Device("Maker-A",
                "PulseOximeter",
                "",
                "5A-8CDC-83EA-1F6E-53A174885BFD",
                "Maker-A PulseOximeter");

        // SpO2
        Vital spO2 = new Vital();
        spO2.setType("SpO2");
        spO2.setIntValue(95);
        spO2.setUnit("%");

        // 脈拍
        Vital pr = new Vital();
        pr.setType("PR");
        pr.setIntValue(98);
        pr.setUnit("回");

        Vitals vitals = new Vitals();
        vitals.setSpO2(spO2);
        vitals.setPr(pr);

        VitalFormat pulseOximeter = new VitalFormat();
        pulseOximeter.setPatient(USER_NAME);
        pulseOximeter.setVitals(vitals);
        pulseOximeter.setDevice(device);

        sendData(pulseOximeter);
    }
}
