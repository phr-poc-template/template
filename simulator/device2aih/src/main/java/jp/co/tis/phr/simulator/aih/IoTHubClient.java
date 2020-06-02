package jp.co.tis.phr.simulator.aih;

import com.microsoft.azure.sdk.iot.device.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
public class IoTHubClient {

    private static String IOTHUB_CONNECT_STRING = System.getenv("IOTHUB_CONNECT_STRING");

    static <T extends IotHubMessage> void sendMessage(T message) throws IOException {

        if (StringUtils.isEmpty(IOTHUB_CONNECT_STRING)) {
            throw new IllegalArgumentException("IoTHub ConnectString is empty.");
        }
        String msgStr = message.serialize();
        log.info(msgStr);
        Message msg = new Message(msgStr);

        DeviceClient client = null;
        try {
            client = new DeviceClient(IOTHUB_CONNECT_STRING, IotHubClientProtocol.MQTT);
            client.open();
            Object lockobj = new Object();
            EventCallback callback = new EventCallback();
            client.sendEventAsync(msg, callback, lockobj);
            synchronized (lockobj) {
                lockobj.wait();
            }
        } catch (URISyntaxException e) {
            log.error("CONNECT_STRINGの形式・内容が不正です。[" + IOTHUB_CONNECT_STRING + "]");
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (client != null) {
                client.closeNow();
            }
        }
    }

    // Print the acknowledgement received from IoT Hub for the telemetry message sent.
    static class EventCallback implements IotHubEventCallback {
        public void execute(IotHubStatusCode status, Object context) {
            System.out.println("IoT Hub responded to message with status: " + status.name());

            if (context != null) {
                synchronized (context) {
                    context.notify();
                }
            }
        }
    }


}
