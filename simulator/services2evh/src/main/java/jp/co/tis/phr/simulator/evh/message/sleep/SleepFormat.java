package jp.co.tis.phr.simulator.evh.message.sleep;

import com.fasterxml.jackson.annotation.JsonProperty;
import jp.co.tis.phr.simulator.evh.EventHubsMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.ZoneId;

@EqualsAndHashCode(callSuper = true)
@Data
public class SleepFormat extends EventHubsMessage {

    @JsonProperty("sleep")
    private Sleep sleep;

    /**
     * jsonに出力するときに日付に同じ値を設定する
     *
     * @return json
     */
    @Override
    public String serialize() {
        Long now = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        setTime(now);
        try {
            BeanUtils.setProperty(sleep, "time", now);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return super.serialize();
    }

}
