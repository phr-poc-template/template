package jp.co.tis.phr.core.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class Event {

    @JsonProperty("time")
    private ZonedDateTime time;

    @JsonProperty("publish")
    private ZonedDateTime publish;

    @JsonProperty("location")
    private String location;

    @JsonProperty("reliability")
    private Integer reliability;

    @JsonProperty("record_type")
    private String recordType;
}
