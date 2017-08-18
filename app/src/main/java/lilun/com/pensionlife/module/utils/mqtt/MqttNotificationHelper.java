package lilun.com.pensionlife.module.utils.mqtt;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.app.Event;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.module.bean.CacheMsg;
import lilun.com.pensionlife.module.utils.CacheMsgClassify;
import lilun.com.pensionlife.module.utils.DeviceUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.ui.home.info_setting.InfoSettingFilter;

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
        CacheMsg cacheMsg = new CacheMsg(data);
        CacheMsgClassify msgClassify = new CacheMsgClassify();


        JSONObject jsonObject = JSON.parseObject(data);
        String title = null;
        String content = null;
        int classify = -1;

        //公告和普通求助
        if (mqttTopic.isTopicBelongCurrentOrganizationLevel(topic) && (topic.contains(mqttTopic.topic_help_suffix) || topic.contains(mqttTopic.topic_information_suffix))) {
            JSONObject infoJson = jsonObject.getJSONObject("data");


            // 1 ----- 公告，展示到通知栏
            if (isMsgICache(InfoSettingFilter.announce) && topic.contains(mqttTopic.topic_information_suffix)) {
                String parentId = infoJson.getString("parentId");
                if (parentId.endsWith("社区公告")) {
                    classify = msgClassify.announce;
                    //发送事件，展示到app
                    EventBus.getDefault().post(new Event.BoardMsg(topic, data));
                    title = "社区公告";
                    content = infoJson.getString("name");
                }
            }


            //2 ----- 普通求助
            if (topic.contains(mqttTopic.topic_help_suffix)) {
                classify = msgClassify.normal_help;

                EventBus.getDefault().post(new Event.RefreshHelpData());
            }
        }


        //3 -----紧急求助
        String topic_help = mqttTopic.urgent_help;
        if (isMsgICache(InfoSettingFilter.help) && TextUtils.equals(topic, topic_help)) {
            classify = msgClassify.urgent_help;
            title = "紧急求助";
            content = "有人需要您的帮助";

//            发送事件，展示到app
            EventBus.getDefault().post(new Event.BoardMsg(topic, data));
        }


        //5-----活动
        String topic_activity = mqttTopic.topic_activity;
        if (mqttTopic.isTopicBelongCurrentOrganizationLevel(topic) && isMsgICache(InfoSettingFilter.activity) && topic.contains(topic_activity)) {
            JSONObject activityJson = jsonObject.getJSONObject("data");
            String categoryId = activityJson.getString("categoryId");
            String category = categoryId.substring(categoryId.lastIndexOf(".") + 1);
            if (!TextUtils.isEmpty(category) && isMsgICache(category)) {
                classify = msgClassify.activity;
                title = "活动";
                String activityTitle = activityJson.getString("title");
                content = "+1社区有新的活动‘" + category + "-" + activityTitle + "’开始了，赶快点我参加吧！";
            }

        }

        //4 ----- 登陆
        if (TextUtils.equals(topic, mqttTopic.login)) {
            dealLogin(data);
        }


        //保存到数据库，绑定用户
        if (classify != -1) {
            cacheMsg.setClassify(classify);
            cacheMsg.save();
        }

        //现实到notification
        show(title, content, topic, jsonObject);

    }


    /**
     * 是否过滤这条消息
     */
    private boolean isMsgICache(String filter) {
        return InfoSettingFilter.isInfoFilter(filter);
    }

    /**
     * 处理登陆
     */
    private void dealLogin(String messageData) {
        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject(messageData);
            String from = jsonObject.getString("from");
            String time = jsonObject.getString("time");
            if (!TextUtils.isEmpty(from)) {
                String clientId = DeviceUtils.getUniqueIdForThisApp(App.context);
                if (!TextUtils.equals(from, clientId)) {
//                        Logger.i("不同设备登陆，此设备下线"+"两个设备id--" + "from--" + from + "---" + "clientId" + clientId);
                    //只有在登录之后的  请求踢账号才有效
                    String loginTime = User.getLoginTime();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = format.parse(loginTime);
                    Logger.d("原账户登录时间是--" + loginTime + "----" + "新登录时间--" + time);
                    if (date != null && date.before(StringUtils.string2Date(time)))
                        EventBus.getDefault().post(new Event.OffLine());
                }
            } else {
//                    Logger.i("相同设备登陆");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    /**
     * 通知栏显示
     */
    private void show(String title, String content, String topic, JSONObject dataJson) {
        if (!TextUtils.isEmpty(title)) {
            MqttTopic mqttTopic = new MqttTopic();

            android.support.v4.app.NotificationCompat.Builder builder = new NotificationCompat.Builder(App.context)
                    .setSmallIcon(R.mipmap.icon)
                    .setContentTitle(title)
                    .setAutoCancel(true)
                    .setContentText(content);

            //点击通知栏后的操作
            MqttNotificationExtra extra = new MqttNotificationExtra();
            Intent intent = new Intent(App.context, MqttNotificationReceiver.class);
            extra.setTopic(topic);

            //活动需要跳转活动列表，需要携带categoryId
            if (topic.contains(mqttTopic.topic_activity)) {
                JSONObject activityJson = dataJson.getJSONObject("data");
                String categoryId = activityJson.getString("categoryId");
                extra.setId(categoryId);
            }


            Bundle bundle = new Bundle();
            bundle.putSerializable("extra", extra);
            intent.putExtras(bundle);
            PendingIntent pIntent = PendingIntent.getBroadcast(App.context, 0, intent, 0);
            builder.setContentIntent(pIntent);


            Notification build = builder.build();
            NotificationManager manager = (NotificationManager) App.context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0x01, build);

            wakeScreen();
        }
    }

    /**
     * 唤醒屏幕
     */
    private void wakeScreen() {
        PowerManager pm = (PowerManager) App.context.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        if (!isScreenOn) {
            PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
            wakeLock.acquire();
            wakeLock.release();
        }

    }


}
