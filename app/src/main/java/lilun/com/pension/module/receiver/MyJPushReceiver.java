package lilun.com.pension.module.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import cn.jpush.android.api.JPushInterface;
import lilun.com.pension.app.Event;
import lilun.com.pension.module.bean.PushMessage;

public class MyJPushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        String notificationTitle = "Pushy";
        String notificationText = "Test notification";

        // Attempt to extract the "message" property from the payload: {"message":"Hello World!"}
        if (intent.getStringExtra("message") != null) {
            notificationText = intent.getStringExtra("message");
        }

        // Prepare a notification with vibration, sound and lights
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setLights(Color.RED, 1000, 1000)
                .setVibrate(new long[]{0, 400, 250, 400})
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        // Get an instance of the NotificationManager service
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        // Build the notification and display it
        notificationManager.notify(1, builder.build());












        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            String content = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            PushMessage pushMessage = new Gson().fromJson(extra, PushMessage.class);
            if (pushMessage!=null){
                PushMessage message = new PushMessage();
                message.setKing(pushMessage.getKing());
                message.setContent(pushMessage.getContent());
                pushMessage.save();
                EventBus.getDefault().post(new Event.RefreshPushMessage());
            }
            Logger.d("收到了自定义消息@@消息内容是:" + content);
            Logger.d("收到了自定义消息@@消息extra是:" + extra);
        }
    }
}