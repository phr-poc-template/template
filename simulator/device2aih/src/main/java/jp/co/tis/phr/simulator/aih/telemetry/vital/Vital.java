package jp.co.tis.phr.simulator.aih.telemetry.vital;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Data
public class Vital {

    @JsonProperty("type")
    private String type;

    @JsonIgnore
    private Integer intValue;

    @JsonIgnore
    private Double doubleValue;

    @JsonProperty("unit")
    private String unit;

    @JsonProperty("time")
    private Long time;

    /**
     * 値にランダムな値を送るためgetValue()だけ実装
     *
     * @return +3の範囲でランダムな値を送信
     */
    @JsonProperty("value")
    public Number getValue() {
        Random random = ThreadLocalRandom.current();
        if (intValue != null) {
            return intValue + random.nextInt(3);
        } else if (doubleValue != null) {
            return doubleValue + random.nextInt(3);
        }
        throw new RuntimeException("intValue or doubleValue == null.");
    }
}
