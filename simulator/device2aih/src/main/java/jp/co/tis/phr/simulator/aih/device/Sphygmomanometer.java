package jp.co.tis.phr.simulator.aih.device;

import jp.co.tis.phr.simulator.aih.Simulator;
import jp.co.tis.phr.simulator.aih.Device;
import jp.co.tis.phr.simulator.aih.telemetry.vital.Vital;
import jp.co.tis.phr.simulator.aih.telemetry.vital.VitalFormat;
import jp.co.tis.phr.simulator.aih.telemetry.vital.Vitals;
import org.quartz.SchedulerException;

public class Sphygmomanometer extends Simulator {

    public static void main(String[] args) throws SchedulerException {
        new Sphygmomanometer().run();
    }

    private void run() throws SchedulerException {

        Device device = new Device("Maker-B",
                "Sphygmomanometer",
                "1.0.0",
                "serialId",
                "Maker-A Sphygmomanometer");

        // 血圧（上）
        Vital bph = new Vital();
        bph.setType("BPH");
        bph.setIntValue(120);
        bph.setUnit("mmHg");

        // 血圧（下）
        Vital bpl = new Vital();
        bpl.setType("BPL");
        bpl.setIntValue(80);
        bpl.setUnit("mmHg");

        // 脈拍
        Vital pr = new Vital();
        pr.setType("PR");
        pr.setIntValue(53);
        pr.setUnit("bpm");

        Vitals vitals = new Vitals();
        vitals.setBpl(bpl);
        vitals.setBph(bph);
        vitals.setPr(pr);

        // 血圧計
        VitalFormat sphygmomanometer = new VitalFormat();
        sphygmomanometer.setPatient(USER_NAME);
        sphygmomanometer.setVitals(vitals);
        sphygmomanometer.setDevice(device);

        sendData(sphygmomanometer);
    }
}
