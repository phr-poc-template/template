package jp.co.tis.phr.simulator.aih;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Data;

@Data
public class IotHubMessage {

    @JsonProperty("patient")
    private String patient;

    @JsonProperty("time")
    private Long time;

    @JsonProperty("device")
    private Device device;

    public String serialize() {
        ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 日付(ZonedDateTime)をISO-8601-1で出力するための設定
        JavaTimeModule jtm = new JavaTimeModule();
        objectMapper.registerModule(jtm);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
