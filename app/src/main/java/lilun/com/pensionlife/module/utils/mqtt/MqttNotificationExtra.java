package lilun.com.pensionlife.module.utils.mqtt;

import java.io.Serializable;

/**
 * Created by Admin on 2017/8/2.
 */
public class MqttNotificationExtra implements Serializable {
    private String topic;
    private String id;

    public String getTopic() {
        return topic;
    }

    public MqttNotificationExtra setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public String getId() {
        return id;
    }

    public MqttNotificationExtra setId(String id) {
        this.id = id;
        return this;
    }
}
