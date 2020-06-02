package jp.co.tis.phr.web.model;

import jp.co.tis.phr.core.entity.UserRecord;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class PulseRate {

    private ZonedDateTime time;

    /**
     * 脈拍
     */
    private Integer pulseRate;

    public static PulseRate extractPulseRate(UserRecord userRecord) {
        return new PulseRate(
                userRecord.getEvent().getTime(),
                userRecord.getPulseRate());
    }

}
