package jp.co.tis.phr.simulator.evh.message.sleep;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Data
public class Sleep {

    @JsonProperty("state")
    private String state;

    @JsonProperty("motion")
    private Integer motion;

    @JsonProperty("time")
    private Long time;

    /**
     * 呼吸
     */
    @JsonProperty("vitalRR")
    private Integer vitalRR;

    /**
     * 心拍
     */
    @JsonProperty("vitalHR")
    private Integer vitalHR;

    /**
     * 値にランダムな値を送るためのGetter
     *
     * @return +3の範囲でランダムな値を送信
     */
    public Integer getVitalRR() {
        Random random = ThreadLocalRandom.current();
        return vitalRR + random.nextInt(3);
    }

    /**
     * 値にランダムな値を送るためのGetter
     *
     * @return +3の範囲でランダムな値を送信
     */
    public Integer getVitalHR() {
        Random random = ThreadLocalRandom.current();
        return vitalHR + random.nextInt(3);
    }
}
