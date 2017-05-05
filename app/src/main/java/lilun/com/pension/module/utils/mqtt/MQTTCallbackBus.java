package lilun.com.pension.module.utils.mqtt;


import com.orhanobut.logger.Logger;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import lilun.com.pension.app.Constants;
import lilun.com.pension.app.Event;
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
        Logger.d("mqtt断开连接");
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


    private PushMessage getPushMessageFromData(String messageData) {
        try {
            PushMessage pushMessage = new PushMessage();
            JSONObject jsonObject = new JSONObject(messageData);
            if (messageData.contains("\"model\"")) {
                String model = (String) jsonObject.get("model");
                pushMessage.setModel(model);
            }
            if (messageData.contains("\"verb\"")) {
                String verb = (String) jsonObject.get("verb");
                pushMessage.setVerb(verb);
            }
            if (messageData.contains("\"data\"")) {
                JSONObject dataJson = (JSONObject) jsonObject.get("data");
                String data = dataJson.toString();
                pushMessage.setData(data);
            }
            if (messageData.contains("\"from\"")) {
                JSONObject dataJson = (JSONObject) jsonObject.get("from");
                String from = dataJson.toString();
                pushMessage.setFrom(from);
            }
            if (messageData.contains("\"to\"")) {
                JSONObject dataJson = (JSONObject) jsonObject.get("to");
                String to = dataJson.toString();
                pushMessage.setTo(to);
            }
            if (messageData.contains("\"message\"")) {
                String message = (String) jsonObject.get("message");
                pushMessage.setMessage(message);
            }
            if (messageData.contains("\"time\"")) {
                String time = (String) jsonObject.get("time");
                pushMessage.setTime(time);
            }

            return pushMessage;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void dealMessage(String topic, String messageData) {
        PushMessage pushMessage = getPushMessageFromData(messageData);

        if (pushMessage != null) {
            if (topic.contains("%23activity")) {  //是活动聊天的数据
                String[] split = topic.split("/");
                if (split.length > 2)
                    pushMessage.setActivityId(split[split.length - 2]);
            }

            pushMessage.save();

            if (Constants.organizationAid.equals(pushMessage.getModel())) {

                EventBus.getDefault().post(pushMessage);
            }
            if ("chat".equals(pushMessage.getVerb())  ||
                    "kiko".equals(pushMessage.getVerb()) || "quit".equals(pushMessage.getVerb())) {
                EventBus.getDefault().post(new Event.RefreshChatAddOne(pushMessage));
            }

        }
    }


}