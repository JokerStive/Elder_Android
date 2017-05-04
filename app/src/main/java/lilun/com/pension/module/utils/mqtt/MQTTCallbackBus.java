package lilun.com.pension.module.utils.mqtt;


import com.orhanobut.logger.Logger;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import lilun.com.pension.app.Constants;
import lilun.com.pension.module.bean.PushMessage;

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
        Logger.i(topic + "====" + message.toString());
        String messageData = message.toString();
        dealMessage(messageData);
    }


    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }


    private PushMessage getPushMessageFromData(String messageData) {
        try {
            PushMessage pushMessage = new PushMessage();
            JSONObject jsonObject = new JSONObject(messageData);
            String model = (String) jsonObject.get("model");
            String verb = (String) jsonObject.get("verb");
            JSONObject dataJson = (JSONObject) jsonObject.get("data");
            String data = dataJson.toString();
            pushMessage.setModel(model);
            pushMessage.setVerb(verb);
            pushMessage.setData(data);
            return pushMessage;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void dealMessage(String messageData) {
        PushMessage pushMessage = getPushMessageFromData(messageData);
        if (pushMessage != null) {
            pushMessage.save();

            if (pushMessage.getModel().equals(Constants.organizationAid)) {

                EventBus.getDefault().post(pushMessage);
            }
        }
    }


}