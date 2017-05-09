package lilun.com.pension.module.utils.mqtt;


import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import lilun.com.pension.app.Event;
import lilun.com.pension.app.User;
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
                JSONArray dataJson = (JSONArray) jsonObject.get("to");



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

            //求助推送
            if (TextUtils.equals(topic,"OrganizationAid/.added") || TextUtils.equals(topic, "OrganizationInformation/.added")) {
                EventBus.getDefault().post(pushMessage);
            }


            dealActivity(topic, pushMessage);

        }
    }


    /**
     * 集中处理丢活动相关Message
     *
     * @param topic
     * @param pushMessage
     */
    public void dealActivity(String topic, PushMessage pushMessage) {
        if (PushMessage.VERB_JOIN.equals(pushMessage.getVerb()) ||
                PushMessage.VERB_CHAR.equals(pushMessage.getVerb()) ||
                PushMessage.VERB_KICK.equals(pushMessage.getVerb()) ||
                PushMessage.VERB_QUIT.equals(pushMessage.getVerb())) {

            if (PushMessage.VERB_KICK.equals(pushMessage.getVerb())) {
                //主持人请出
                //发送强制退出聊天
                if (pushMessage.getTo().contains(User.getUserId())) {
                    EventBus.getDefault().post(new Event.ForcedQuitChat("您被主持人请出了本活动"));
                    //发送刷新我的活动
                    EventBus.getDefault().post(new Event.RefreshActivityData());
                    //取消订阅
                    MQTTManager.getInstance().unsubscribe(topic, null, null);

                }
            } else if (PushMessage.VERB_QUIT.equals(pushMessage.getVerb())) {

                //被动强制退出
                if (TextUtils.isEmpty(pushMessage.getFrom())) {
                    EventBus.getDefault().post(new Event.ForcedQuitChat(pushMessage.getMessage()));
                    //发送刷新我的活动
                    EventBus.getDefault().post(new Event.RefreshActivityData());
                } else {
                    //是主动退出活动，
                    //是主动退出是否自己
                    if (pushMessage.getFrom().contains(User.getUserId())) {
                        //发送刷新我的活动
                        EventBus.getDefault().post(new Event.RefreshActivityData());
                        //取消订阅
                        MQTTManager.getInstance().unsubscribe(topic, null, null);
                    }
                    //其他人添加信息
                }

            }
            EventBus.getDefault().post(new Event.RefreshChatAddOne(pushMessage));
        }
    }


}