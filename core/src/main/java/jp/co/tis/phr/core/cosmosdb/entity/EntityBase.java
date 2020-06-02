package jp.co.tis.phr.core.cosmosdb.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EntityBase {

    @JsonProperty("id")
    private String id;

    @JsonProperty("deleted")
    private boolean deleted;

    /**
     * TTLは無期限(既定）
     */
    @JsonProperty("ttl")
    private int ttl = -1;

}
