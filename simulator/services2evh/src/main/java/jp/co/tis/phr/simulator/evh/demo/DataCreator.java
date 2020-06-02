package jp.co.tis.phr.simulator.evh.demo;


import jp.co.tis.phr.simulator.evh.EventHubsMassDataClient;
import jp.co.tis.phr.simulator.evh.Device;
import jp.co.tis.phr.simulator.evh.EventHubsMessage;
import jp.co.tis.phr.simulator.evh.message.vital.Vital;
import jp.co.tis.phr.simulator.evh.message.vital.VitalFormat;
import jp.co.tis.phr.simulator.evh.message.vital.Vitals;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataCreator extends EventHubsMassDataClient {

    protected static final String USER_NAME
            = StringUtils.isNotEmpty(System.getenv("USER_NAME")) ? System.getenv("USER_NAME") : "demo01";

    // ユーザ 複数人分のデータを作成する場合は、Arrays.asList("demo01","demo02","demo03")
    private final List<String> USERS = Arrays.asList(USER_NAME);

    public static void main(String[] args) {
        new DataCreator().run();
    }

    private void run() throws RuntimeException {

        // 何時間ごとにレコードをつくるか
        final int interval = 4;
        // 1日当たりのレコード数
        final int recordsPerDay = 24 / interval;
        // 2週間前からのデータを作成
        final int recordsWeeks = 2;
        // 15日分のデータを作成
        final int totalRecords = (recordsPerDay * recordsWeeks + 1) * 7;

        // 2週間前の0:00からデータを作成する
        LocalDateTime standardDateTime
                = LocalDateTime.now().withHour(1).withMinute(0).withSecond(0)
                .minusWeeks(recordsWeeks);

        List<String> messages = new ArrayList<>();
        int addHour = 0;
        for (int i = 1; i <= totalRecords; i++) {
            final int cnt = i;
            final int hour = addHour;
            // 4時間おきのデータを作成
            USERS.forEach(u ->
                    messages.add(serialize(createData(Integer.toString(cnt), u, standardDateTime.plusHours(hour * interval)))));
            addHour++;
        }
        try {
            sendMessage(messages);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static EventHubsMessage createData(String serialId, String user, LocalDateTime dateTime) {

        // GoogleHome
        Device device = new Device("Maker-A",
                "VoiceAssistant",
                "2.0",
                serialId,
                "Maker-A VoiceAssistant");

        // 体温
        Vital bt = new Vital();
        bt.setType("BT");
        bt.setDoubleValue(36.2);
        bt.setUnit("℃");

        // 血圧（上）
        Vital bph = new Vital();
        bph.setType("BPH");
        bph.setIntValue(138);
        bph.setUnit("mmHg");

        // 血圧（下）
        Vital bpl = new Vital();
        bpl.setType("BPL");
        bpl.setIntValue(86);
        bpl.setUnit("mmHg");

        // 脈拍
        Vital pr = new Vital();
        pr.setType("PR");
        pr.setIntValue(53);
        pr.setUnit("bpm");

        // SpO2
        Vital spO2 = new Vital();
        spO2.setType("SpO2");
        spO2.setIntValue(97);
        spO2.setUnit("%");

        // 血糖値
        Vital bs = new Vital();
        bs.setType("BS");
        bs.setIntValue(105);
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
        voiceAssistant.setVitals(vitals);
        voiceAssistant.setPatient(user);
        voiceAssistant.setDevice(device);
        voiceAssistant.setTime(dateTime.atZone(ZoneId.systemDefault()).toEpochSecond());

        return voiceAssistant;

    }
}
