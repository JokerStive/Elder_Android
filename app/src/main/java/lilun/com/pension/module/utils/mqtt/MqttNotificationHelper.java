package lilun.com.pension.module.utils.mqtt;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.PowerManager;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.app.User;

/**
 * 需要展示到通知栏的mqtt消息
 * 因为格式不一致，具体约定参见：https://oa.liluntech.com/projects/sce/wiki/%E6%8E%A8%E9%80%81%E8%A7%84%E8%8C%83%E7%BA%A6%E5%AE%9A
 *
 * @author yk
 *         create at 2017/6/7 15:34
 *         email : yk_developer@163.com
 */
public class MqttNotificationHelper {

    public void showOnNotification(String topic, String data) {
        MqttTopic mqttTopic = new MqttTopic();
        JSONObject jsonObject = JSON.parseObject(data);
        String title = null;
        String content = null;

        //公告消息
        // 是基于模型的,只有是自己所在的市级以下发布的公告才展示
        if (TextUtils.equals(topic, mqttTopic.normal_announce)) {
            JSONObject infoJson = jsonObject.getJSONObject("data");
            String organizationId = infoJson.getString("organizationId");
            boolean isShow = User.canOperate(organizationId);
            if (isShow) {
                title = "公告";
                content = infoJson.getString("name");
            }
        }


        //紧急消息
        if (TextUtils.equals(topic, mqttTopic.urgent_help)) {
            title = "紧急求助";
            content = jsonObject.getString("message");
        }


        show(title, content);

    }

    /**
    *通知栏显示
    */
    private void show(String title, String content) {
        if (!TextUtils.isEmpty(title)) {
            Notification build = new NotificationCompat.Builder(App.context)
                    .setSmallIcon(R.drawable.small_icon)
                    .setContentTitle(title)
                    .setContentText(content).build();

            NotificationManager manager =
                    (NotificationManager) App.context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0x01, build);

            wakeScreen();
        }
    }

    /**
    *唤醒屏幕
    */
    private void wakeScreen() {
        PowerManager pm = (PowerManager) App.context.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        if (!isScreenOn){
            PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
            wakeLock.acquire();
            wakeLock.release();
        }

    }
}
