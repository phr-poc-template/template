package jp.co.tis.phr.web.model;

import jp.co.tis.phr.core.entity.UserRecord;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class BodyTemperature {
    private ZonedDateTime time;

    /**
     * 体温
     */
    private BigDecimal bodyTemperature;

    public static BodyTemperature extractBodyTemperature(UserRecord userRecord) {
        return new BodyTemperature(
                userRecord.getEvent().getTime(),
                userRecord.getBodyTemperature());
    }
}
