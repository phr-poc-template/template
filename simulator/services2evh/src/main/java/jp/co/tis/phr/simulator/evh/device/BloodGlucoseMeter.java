package jp.co.tis.phr.simulator.evh.device;

import jp.co.tis.phr.simulator.evh.Device;
import jp.co.tis.phr.simulator.evh.message.vital.Vital;
import jp.co.tis.phr.simulator.evh.message.vital.VitalFormat;
import jp.co.tis.phr.simulator.evh.message.vital.Vitals;
import jp.co.tis.phr.simulator.evh.Simulator;
import org.quartz.SchedulerException;

/**
 * 血糖値計
 */
public class BloodGlucoseMeter extends Simulator {

    public static void main(String[] args) throws SchedulerException {
        new BloodGlucoseMeter().run();
    }

    private void run() {

        Device device = new Device("Maker-A",
                "BloodGlucoseMeter",
                "",
                "565654F8-1781-842E-0634-AD6E9138E5ED",
                "Maker-A BloodGlucoseMeter");

        // 血糖値
        Vital bs = new Vital();
        bs.setType("BS");
        bs.setIntValue(297);
        bs.setUnit("mg/dL");

        Vitals vitals = new Vitals();
        vitals.setBs(bs);

        VitalFormat bloodGlucose = new VitalFormat();
        bloodGlucose.setPatient(USER_NAME);
        bloodGlucose.setVitals(vitals);
        bloodGlucose.setDevice(device);

        sendData(bloodGlucose);
    }
}
