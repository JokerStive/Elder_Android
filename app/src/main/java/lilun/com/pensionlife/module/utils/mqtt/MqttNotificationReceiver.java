package lilun.com.pensionlife.module.utils.mqtt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.greenrobot.eventbus.EventBus;

import lilun.com.pensionlife.ui.home.HomeActivity;

/**
 * 通知栏点击统一处理
 *
 * @author yk
 *         create at 2017/8/2 9:29
 *         email : yk_developer@163.com
 */
public class MqttNotificationReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        MqttNotificationExtra extra = (MqttNotificationExtra) intent.getSerializableExtra("extra");
        if (extra != null) {
            Intent newIntent = new Intent(context, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(newIntent);
            EventBus.getDefault().post(extra);
        }
    }

}
