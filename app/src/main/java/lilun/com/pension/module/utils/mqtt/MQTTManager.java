package lilun.com.pension.module.utils.mqtt;


import android.text.TextUtils;
import android.util.Log;

import com.orhanobut.logger.Logger;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import lilun.com.pension.app.App;
import lilun.com.pension.app.Config;
import lilun.com.pension.module.utils.DeviceUtils;


/**
 * MQTT管理
 *
 * @author yk
 *         create at 2017/4/25 9:46
 *         email : yk_developer@163.com
 */
public class MQTTManager {

    // 单例
    private static MQTTManager mInstance = null;

    // 回调
    private MqttCallback mCallback;

    private MqttAndroidClient client;

    private MQTTManager() {
        mCallback = new MQTTCallbackBus();
    }

    public static MQTTManager getInstance() {
        if (null == mInstance) {
            mInstance = new MQTTManager();
        }
        return mInstance;
    }

    /**
     * 释放单例, 及其所引用的资源
     */
    public static void release() {
        try {
            if (mInstance != null) {
//                mInstance.client.unsubscribe(MQTTTopicUtils.initTopic());
                mInstance.disConnect();
                mInstance = null;
            }


        } catch (Exception e) {

        }
    }

    public boolean isConnected() {
        return client != null && client.isConnected();
    }
//
//    public void createConnect(String userName, String password, String[] topics, int[] qos) {
//        if (client != null && client.isConnected()) {
//            Logger.i("mqtt 已经链接不需要再次链接");
//            return;
//        }
//
//        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
//            return;
//        }
//        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
//        mqttConnectOptions.setAutomaticReconnect(true);
//        mqttConnectOptions.setCleanSession(false);
//
//        mqttConnectOptions.setPassword(password.toCharArray());
//
//        mqttConnectOptions.setUserName(userName);
//        if (client == null) {
//            String deviceId = DeviceUtils.getUniqueIdForThisApp(App.context);
//            client = new MqttAndroidClient(App.context, Config.MQTT_URL, deviceId);
////            Logger.i("设备Id:" + deviceId);
//        }
//
//        client.setCallback(mCallback);
//        try {
//            client.connect(mqttConnectOptions, null, new IMqttActionListener() {
//                @Override
//                public void onSuccess(IMqttToken asyncActionToken) {
//                    Logger.i("连接mqtt服务器成功");
//                    if (topics != null && qos != null && topics.length == qos.length)
//                        subscribe(topics, qos);
//                }
//
//                @Override
//                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//                    Logger.i("链接失败" + exception.getMessage());
//                }
//            });
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
//
//
//    }


    public void connect(String userName, String password, IMqttActionListener listener) {

        if (client != null && client.isConnected()) {
            Logger.i("mqtt 已经链接不需要再次链接");
            return;
        }
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            return;
        }
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);

        mqttConnectOptions.setPassword(password.toCharArray());

        mqttConnectOptions.setUserName(userName);


        if (client == null) {
            String deviceId = DeviceUtils.getUniqueIdForThisApp(App.context);
            client = new MqttAndroidClient(App.context, Config.MQTT_URL, deviceId);
            Logger.i("设备Id:" + deviceId);
            client.setCallback(mCallback);
        }

        if (client!=null){
//            client.clearAbortBroadcast();
        }

        try {
            client.connect(mqttConnectOptions, null, listener);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    /**
     * 发送消息
     */
    public void publish(String publishTopic, int qos, String publishMessage) {
        if (client != null && client.isConnected()) {
            try {
                MqttMessage message = new MqttMessage();

                message.setQos(qos);
                message.setPayload(publishMessage.getBytes());
                IMqttDeliveryToken token = client.publish(publishTopic, message);
                Logger.i("发送数据:" + token.getMessage());

            } catch (MqttException e) {
                Log.d("yk", e.getMessage());
            }

        }

    }


    /**
     * 发送消息
     */
    public void publish(String publishTopic, int qos, String publishMessage,boolean isRetain) {
        if (client != null && client.isConnected()) {
            try {
                MqttMessage message = new MqttMessage();
//                message.setQos(qos);
                message.setPayload(publishMessage.getBytes());
                IMqttDeliveryToken token = client.publish(publishTopic, publishMessage.getBytes(),qos,isRetain);
                Logger.i("发送数据:" + token.getMessage());

            } catch (MqttException e) {
                Log.d("yk", e.getMessage());
            }

        }

    }

    public void subscribe(String topic, int qos) {
        try {
            client.subscribe(topic, qos, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
//                    ACache.get(App.context, "topics").put(topic, topic);
                    Logger.i(topic + "---订阅成功");
//                    Logger.i("缓存topic--" + topic);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Logger.i(topic + "---订阅失败");
                }
            });

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }




    public void subscribe(String topic, int qos, IMqttActionListener listener) {
        try {
            client.subscribe(topic, qos, null, listener);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void unSubscribe(String topic, Object usertext, IMqttActionListener listener) {
        try {
            client.unsubscribe(topic, usertext, listener);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    public void subscribe(String[] topics, int[] qos) {
        try {
            client.subscribe(topics, qos, null, new IMqttActionListener() {

                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    for (String topic : topics) {
                        Logger.i(topic + "---订阅成功");
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    for (String topic : topics) {
                        Logger.i(topic + "---订阅失败");
//                        ToastHelper.get().showShort("链接失败" + exception.getMessage()+"您将收不到消息");
                    }
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    /**
     * 取消连接
     *
     * @throws MqttException
     */
    public void disConnect() throws MqttException {
        if (client != null && client.isConnected()) {
            client.disconnect();
            client = null;
        }
    }
}