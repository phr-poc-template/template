package jp.co.tis.phr.simulator.evh.message.vital;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
     * @return typeに合わせてランダムな値を返却する
     */
    @JsonProperty("value")
    public Number getRdandomValue() {
        Random random = ThreadLocalRandom.current();

        if ("BT".equals(type)) {
            // 体温
            return new BigDecimal(doubleValue + random.nextDouble()/4).setScale(1, RoundingMode.DOWN);
        } else if ("BPH".equals(type) || "BPL".equals(type)) {
            // 拡張期血圧・収縮期血圧
            return intValue + random.nextInt(2);
        } else if ("PR".equals(type) || "RR".equals(type)) {
            // 脈拍・呼吸数
            return intValue + random.nextInt(3);
        } else if ("SpO2".equals(type)) {
            // SpO2
            return intValue + random.nextInt(2);
        } else if ("BS".equals(type)) {
            // 血糖値
            return intValue + random.nextInt(2);
        } else if ("BH".equals(type) || "BW".equals(type)) {
            // 身長・体重
            return new BigDecimal(doubleValue + random.nextDouble()).setScale(1, RoundingMode.DOWN);
        }
        throw new RuntimeException("type not found. type = " + type);
    }
}
