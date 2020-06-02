package jp.co.tis.phr.simulator.aih.telemetry.vital;

import com.fasterxml.jackson.annotation.JsonProperty;
import jp.co.tis.phr.simulator.aih.IotHubMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.ZoneId;

@EqualsAndHashCode(callSuper = true)
@Data
public class VitalFormat extends IotHubMessage {

    @JsonProperty("vitals")
    private Vitals vitals;

    /**
     * jsonに出力するときに日付に同じ値を設定する
     *
     * @return json
     */
    @Override
    public String serialize() {
        Long now = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();
        setTime(now);
        vitals.list().forEach(v -> {
            if (v == null) {
                return;
            }
            try {
                BeanUtils.setProperty(v, "time", now);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });
        return super.serialize();
    }

}
