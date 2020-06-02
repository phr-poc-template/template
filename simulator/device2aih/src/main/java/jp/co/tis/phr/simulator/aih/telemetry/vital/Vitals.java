package jp.co.tis.phr.simulator.aih.telemetry.vital;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class Vitals {

    @JsonProperty("BT")
    private Vital bt;

    @JsonProperty("BPH")
    private Vital bph;

    @JsonProperty("BPL")
    private Vital bpl;

    @JsonProperty("PR")
    private Vital pr;

    @JsonProperty("BS")
    private Vital bs;

    @JsonProperty("SpO2")
    private Vital spO2;

    @JsonProperty("BH")
    private Vital bh;

    @JsonProperty("BW")
    private Vital bw;

    @JsonProperty("RR")
    private Vital rr;

    public List<Vital> list() {
        return Arrays.asList(bt, bph, bpl, pr);
    }
}
