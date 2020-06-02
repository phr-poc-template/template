package jp.co.tis.phr.simulator.evh.device;

import jp.co.tis.phr.simulator.evh.Device;
import jp.co.tis.phr.simulator.evh.message.vital.Vital;
import jp.co.tis.phr.simulator.evh.message.vital.VitalFormat;
import jp.co.tis.phr.simulator.evh.message.vital.Vitals;
import jp.co.tis.phr.simulator.evh.Simulator;
import org.quartz.SchedulerException;

/**
 * 血圧計
 */
public class Sphygmomanometer extends Simulator {

    public static void main(String[] args) throws SchedulerException {
        new Sphygmomanometer().run();
    }

    private void run() {

        Device device = new Device("Maker-A",
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
