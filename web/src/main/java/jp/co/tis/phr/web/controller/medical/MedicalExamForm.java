package jp.co.tis.phr.web.controller.medical;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class MedicalExamForm {

    /**
     * 受診日
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate eventTime;

    /**
     * 受診会場
     */
    @NotEmpty
    private String eventLocation;

    /**
     * 身長
     */
    @NotNull
    @Min(10)
    @Max(300)
    @Digits(integer = 3, fraction = 1)
    private Double height;

    /**
     * 体重
     */
    @NotNull
    @Min(1)
    @Max(300)
    @Digits(integer = 3, fraction = 1)
    private Double weight;

    /**
     * 収縮期血圧
     */
    @NotNull
    @Min(10)
    @Max(300)
    private Integer systolicBloodPressure;

    /**
     * 拡張期血圧
     */
    @NotNull
    @Min(10)
    @Max(300)
    private Integer diastolicBloodPressure;

    /**
     * LDLコレステロール
     */
    @NotNull
    @Min(0)
    @Max(1000)
    private Integer ldlCholesterol;

    /**
     * HDLコレステロール
     */
    @NotNull
    @Min(0)
    @Max(300)
    private Integer hdlCholesterol;

    /**
     * 喫煙
     */
    @NotEmpty
    @Pattern(regexp = "あり|なし|過去にあり")
    private String tobaccoHistory;

    /**
     * 尿蛋白
     */
    @NotEmpty
    @Pattern(regexp = "－|±|＋|2＋|3＋以上")
    private String urineProtein;

    /**
     * 血糖
     */
    @NotNull
    @Min(0)
    @Max(3000)
    private Integer fastingPlasmaGlucose;

    /**
     * HbA1c
     */
    @NotNull
    @Min(0)
    @Max(30)
    @Digits(integer = 2, fraction = 1)
    private Double hbA1c;

    /**
     * ALT
     */
    @NotNull
    @Min(0)
    @Max(10000)
    private Integer alt;

    /**
     * 中性脂肪
     */
    @NotNull
    @Min(0)
    @Max(10000)
    private Integer neutralFat;

    /**
     * AST
     */
    @NotNull
    @Min(0)
    @Max(10000)
    private Integer ast;

    /**
     * 腹囲
     */
    @NotNull
    @Min(10)
    @Max(300)
    private Integer waistCircumference;

    /**
     * 尿糖
     */
    @NotEmpty
    @Pattern(regexp = "－|±|＋|2＋以上")
    private String urinaryGlucose;

    /**
     * gammaGTP
     */
    @NotNull
    @Min(0)
    @Max(10000)
    private Integer gammaGtp;
}
