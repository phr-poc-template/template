package jp.co.tis.phr.core.cosmosdb.annotation;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Document {

    String DEFAULT_CONTAINER_NAME = "";
    String DEFAULT_REQUEST_UNIT = "400";
    int DEFAULT_TIME_TO_LIVE = -1; // Indicates never expire

    String container() default DEFAULT_CONTAINER_NAME;

    String ru() default DEFAULT_REQUEST_UNIT;

    int timeToLive() default DEFAULT_TIME_TO_LIVE;
}
