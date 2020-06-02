package jp.co.tis.phr.simulator.evh;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.microsoft.azure.eventhubs.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 大量にデータを送信するためのクラス。
 * 1メッセージの上限である256KByte区切りでデータを送信することができます。
 * 継承して使ってください。
 */
@Slf4j
public class EventHubsMassDataClient {

    private static String EVENTHUBS_CONNECT_STRING = System.getenv("EVENTHUBS_CONNECT_STRING");

    protected void sendMessage(List<String> messages)
            throws IOException, EventHubException, InterruptedException {

        if (StringUtils.isEmpty(EVENTHUBS_CONNECT_STRING)) {
            throw new IllegalArgumentException("EventHub Connect String is empty.");
        }
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(4);
        EventHubClient ehClient = EventHubClient.createFromConnectionStringSync(EVENTHUBS_CONNECT_STRING, executorService);

        long startTime = System.currentTimeMillis();

        try {
            final EventDataBatch eventDataBatch = ehClient.createBatch();
            for (int i = 0; i < messages.size() - 1; ) {
                i = sendBatch(ehClient, messages, i);
                System.out.println("sleep 1 minutes....");
                Thread.sleep(1000);
            }
        } finally {
            ehClient.closeSync();
            executorService.shutdown();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("処理時間：" + (endTime - startTime) + " ms");
    }

    private int sendBatch(EventHubClient ehClient, List<String> messages, int start) throws EventHubException {
        final EventDataBatch eventDataBatch = ehClient.createBatch();
        int i = start;
        for (; i < messages.size(); i++) {
            String msgStr = messages.get(i);
            System.out.println(msgStr);
            byte[] payloadBytes = msgStr.getBytes(Charset.defaultCharset());
            EventData eventData = EventData.create(payloadBytes);
            // 256KByteまでデータを詰める
            if (!eventDataBatch.tryAdd(eventData)) {
                break;
            }
        }
        ehClient.sendSync(eventDataBatch);
        System.out.println(String.format("--->%d件送信済", i));
        return i;
    }

    protected String serialize(EventHubsMessage eventHubsMessage) {

        ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 日付(ZonedDateTime)をISO-8601-1で出力するための設定
        JavaTimeModule jtm = new JavaTimeModule();
        objectMapper.registerModule(jtm);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            return objectMapper.writeValueAsString(eventHubsMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
