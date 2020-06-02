package jp.co.tis.phr.simulator.aih;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Device {

    @JsonProperty("makerCode")
    private String makerCode;

    @JsonProperty("productCode")
    private String productCode;

    @JsonProperty("version")
    private String version;

    @JsonProperty("serialId")
    private String serialId;

    @JsonProperty("name")
    private String name;

}
