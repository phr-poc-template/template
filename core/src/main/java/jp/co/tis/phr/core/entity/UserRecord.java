package jp.co.tis.phr.core.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jp.co.tis.phr.core.cosmosdb.annotation.Document;
import jp.co.tis.phr.core.cosmosdb.entity.EntityBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(container = "user_record")
public class UserRecord extends EntityBase {

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("event")
    private Event event;

    /**
     * 身長
     */
    @JsonProperty("height")
    private Double height;

    /**
     * 体重
     */
    @JsonProperty("weight")
    private Double weight;

    /**
     * 収縮期血圧
     */
    @JsonProperty("systolic_blood_pressure")
    private Integer systolicBloodPressure;

    /**
     * 拡張期血圧
     */
    @JsonProperty("diastolic_blood_pressure")
    private Integer diastolicBloodPressure;

    /**
     * LDLコレステロール
     */
    @JsonProperty("ldl_cholesterol")
    private Integer ldlCholesterol;

    /**
     * HDLコレステロール
     */
    @JsonProperty("hdl_cholesterol")
    private Integer hdlCholesterol;

    /**
     * 喫煙
     */
    @JsonProperty("tobacco_history")
    private String tobaccoHistory;

    /**
     * 尿蛋白
     */
    @JsonProperty("urine_protein")
    private String urineProtein;

    /**
     * 血糖
     */
    @JsonProperty("fasting_plasma_glucose")
    private Integer fastingPlasmaGlucose;

    /**
     * HbA1c
     */
    @JsonProperty("hbA1c")
    private Double hbA1c;

    /**
     * ALT
     */
    @JsonProperty("alt")
    private Integer alt;

    /**
     * 中性脂肪
     */
    @JsonProperty("neutral_fat")
    private Integer neutralFat;

    /**
     * AST
     */
    @JsonProperty("ast")
    private Integer ast;

    /**
     * 腹囲
     */
    @JsonProperty("waist_circum_ference")
    private Integer waistCircumference;

    /**
     * 尿糖
     */
    @JsonProperty("urinary_glucose")
    private String urinaryGlucose;

    /**
     * gammaGTP
     */
    @JsonProperty("gamma_Gtp")
    private Integer gammaGtp;

    /**
     * 脈拍
     */
    @JsonProperty("pulse_rate")
    private Integer pulseRate;

    /**
     * 体温
     */
    @JsonProperty("body_temperature")
    private BigDecimal bodyTemperature;

    /**
     * SpO2
     */
    @JsonProperty("spO2")
    private Integer spO2;

    @Override
    public String getId() {
        // UserIdを名前空間、イベント時刻を名前としてUUIDを生成
        String source = userId + event.getTime().toInstant().toEpochMilli();
        byte[] bytes = source.getBytes(Charset.defaultCharset());
        return UUID.nameUUIDFromBytes(bytes).toString();
    }
}
