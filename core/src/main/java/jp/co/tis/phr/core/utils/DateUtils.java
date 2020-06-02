package jp.co.tis.phr.core.utils;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public final class DateUtils {

    public static final ZonedDateTime nowUTC() {
        return ZonedDateTime.now(ZoneId.of("UTC"));
    }
}
