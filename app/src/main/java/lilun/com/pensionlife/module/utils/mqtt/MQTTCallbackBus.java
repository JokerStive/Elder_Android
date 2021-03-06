package lilun.com.pensionlife.module.utils.mqtt;


import com.orhanobut.logger.Logger;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * 使用EventBus分发事件
 *
 * @author LichFaker on 16/3/25.
 * @Email lichfaker@gmail.com
 */
public class MQTTCallbackBus implements MqttCallback {

    @Override
    public void connectionLost(Throwable cause) {
//        Logger.d("mqtt断开连接",cause.getMessage());
    }


    @Override
    public void messageArrived(String topic, MqttMessage message) {
        Logger.i(topic + "====\n" + message.toString());
        String messageData = message.toString();
        dealMessage(topic, messageData);
    }


    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }


    private void dealMessage(String topic, String messageData) {

        showOnNotification(topic, messageData);

        //处理活动相关的消息
        new MqttActivityHelper(topic, messageData);

    }

    /**
     * 在通知栏展示
     */
    private void showOnNotification(String topic, String pushMessage) {
        MqttNotificationHelper mqttNotificationHelper = new MqttNotificationHelper();
        mqttNotificationHelper.showOnNotification(topic, pushMessage);
    }

}