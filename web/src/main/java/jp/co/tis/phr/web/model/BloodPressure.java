package jp.co.tis.phr.web.model;

import jp.co.tis.phr.core.entity.UserRecord;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class BloodPressure {

    private ZonedDateTime time;

    /**
     * 収縮期血圧
     */
    private Integer systolicBloodPressure;

    /**
     * 拡張期血圧
     */
    private Integer diastolicBloodPressure;

    public static BloodPressure extractBloodPressure(UserRecord userRecord) {
        return new BloodPressure(
                userRecord.getEvent().getTime(),
                userRecord.getSystolicBloodPressure(),
                userRecord.getDiastolicBloodPressure());
    }

}
