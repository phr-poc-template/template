package jp.co.tis.phr.core.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jp.co.tis.phr.core.cosmosdb.annotation.Document;
import jp.co.tis.phr.core.cosmosdb.entity.EntityBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.ZonedDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(container = "user")
public class User extends EntityBase {

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("registration_date")
    private ZonedDateTime registrationDate;

}
