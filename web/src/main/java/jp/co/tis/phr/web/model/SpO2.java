package jp.co.tis.phr.web.model;

import jp.co.tis.phr.core.entity.UserRecord;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class SpO2 {

    private ZonedDateTime time;

    /**
     * SpO2
     */
    private Integer spO2;

    public static SpO2 extractSpO2(UserRecord userRecord) {
        return new SpO2(
                userRecord.getEvent().getTime(),
                userRecord.getSpO2());
    }
}
