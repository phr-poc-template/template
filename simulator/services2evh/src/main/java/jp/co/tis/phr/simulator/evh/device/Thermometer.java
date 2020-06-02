package jp.co.tis.phr.simulator.evh.device;

import jp.co.tis.phr.simulator.evh.Device;
import jp.co.tis.phr.simulator.evh.message.vital.Vital;
import jp.co.tis.phr.simulator.evh.message.vital.VitalFormat;
import jp.co.tis.phr.simulator.evh.message.vital.Vitals;
import jp.co.tis.phr.simulator.evh.Simulator;
import org.quartz.SchedulerException;

public class Thermometer extends Simulator {

    public static void main(String[] args) throws SchedulerException {
        new Thermometer().run();
    }

    private void run() {

        Device device = new Device("Maker-A",
                "Thermometer",
                "1.0.0",
                "serialId",
                "Maker-A Thermometer");

        Vital bt = new Vital();
        bt.setType("BT");
        bt.setDoubleValue(36.6);
        bt.setUnit("℃");
        Vitals vitals = new Vitals();
        vitals.setBt(bt);

        // 体温計
        VitalFormat thermometer = new VitalFormat();
        thermometer.setPatient(USER_NAME);
        thermometer.setVitals(vitals);
        thermometer.setDevice(device);

        sendData(thermometer);
    }
}
