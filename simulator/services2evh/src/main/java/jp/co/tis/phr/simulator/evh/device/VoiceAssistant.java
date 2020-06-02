package jp.co.tis.phr.simulator.evh.device;

import jp.co.tis.phr.simulator.evh.Device;
import jp.co.tis.phr.simulator.evh.message.vital.Vital;
import jp.co.tis.phr.simulator.evh.message.vital.VitalFormat;
import jp.co.tis.phr.simulator.evh.message.vital.Vitals;
import jp.co.tis.phr.simulator.evh.Simulator;

/**
 * Voice Assistant
 */
public class VoiceAssistant extends Simulator {

    public static void main(String[] args) {
        new VoiceAssistant().run();
    }

    private void run() {

        // GoogleHome
        Device device = new Device("Maker-A",
                "VoiceAssistant",
                "2.0",
                "Serial-ID",
                "Maker-A VoiceAssistant");

        // BT
        Vital bt = new Vital();
        bt.setType("BT");
        bt.setDoubleValue(36.6);
        bt.setUnit("℃");

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

        // SpO2
        Vital spO2 = new Vital();
        spO2.setType("SpO2");
        spO2.setIntValue(95);
        spO2.setUnit("%");

        // 血糖値
        Vital bs = new Vital();
        bs.setType("BS");
        bs.setIntValue(297);
        bs.setUnit("mg/dL");

        // 以下は2019/11現在CDC側で対応していないが、今後のことを考えて実装しておく
        // BH:身長, BW:体重, RR:呼吸数
        Vital bh = new Vital();
        bh.setType("BH");
        bh.setDoubleValue(180.0);
        bh.setUnit("cm");

        Vital bw = new Vital();
        bw.setType("BW");
        bw.setDoubleValue(74.3);
        bw.setUnit("kg");

        Vital rr = new Vital();
        rr.setType("RR");
        rr.setIntValue(20);
        rr.setUnit("回");

        Vitals vitals = new Vitals();
        vitals.setBt(bt);
        vitals.setBph(bph);
        vitals.setBpl(bpl);
        vitals.setPr(pr);
        vitals.setSpO2(spO2);
        vitals.setBs(bs);
        vitals.setBh(bh);
        vitals.setBw(bw);
        vitals.setRr(rr);

        VitalFormat voiceAssistant = new VitalFormat();
        voiceAssistant.setPatient(USER_NAME);
        voiceAssistant.setVitals(vitals);
        voiceAssistant.setDevice(device);

        sendData(voiceAssistant);
    }
}
