package jp.co.tis.phr.simulator.evh;

import com.microsoft.azure.eventhubs.ConnectionStringBuilder;
import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.eventhubs.EventHubClient;
import com.microsoft.azure.eventhubs.EventHubException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
public class EventHubsClient {

    private static String EVENTHUBS_CONNECT_STRING = System.getenv("EVENTHUBS_CONNECT_STRING");

    public static <T extends EventHubsMessage> void sendMessage(T message) throws IOException, EventHubException {

        String msgStr = message.serialize();
        log.info(msgStr);

        if (StringUtils.isEmpty(EVENTHUBS_CONNECT_STRING)) {
            throw new IllegalArgumentException("EventHub Connect String is empty.");
        }

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(4);
        EventHubClient ehClient = EventHubClient.createFromConnectionStringSync(EVENTHUBS_CONNECT_STRING, executorService);
        try {
            byte[] payloadBytes = msgStr.getBytes(Charset.defaultCharset());
            EventData sendEvent = EventData.create(payloadBytes);
            ehClient.sendSync(sendEvent);
        } finally {
            ehClient.closeSync();
            executorService.shutdown();
        }
    }
}
