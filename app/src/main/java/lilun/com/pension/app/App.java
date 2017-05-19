package lilun.com.pension.app;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.baidu.mapapi.SDKInitializer;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.Date;

import lilun.com.pension.BuildConfig;
import lilun.com.pension.module.utils.ACache;
import lilun.com.pension.module.utils.DeviceUtils;
import lilun.com.pension.module.utils.PreUtils;
import lilun.com.pension.module.utils.mqtt.MQTTManager;

/**
 * 入口
 *
 * @author yk
 *         create at 2017/2/4 13:40
 *         email : yk_developer@163.com
 */
public class App extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        init();

    }

    private void init() {

        //日志
        if (BuildConfig.LOG_DEBUG) {
            Logger.init(Config.TAG_LOGGER).methodCount(1).hideThreadInfo();
        } else {
            Logger.init(Config.TAG_LOGGER).logLevel(LogLevel.NONE);
        }

        //内存泄漏
//        LeakCanary.install(this);


//        数据库
        LitePal.initialize(this);

        SQLiteDatabase db = LitePal.getDatabase();


//       百度地图
        SDKInitializer.initialize(this);

    }


    public static void resetMQTT() {
        Logger.i("尝试重链接mqtt");
        MQTTManager mqttManager = MQTTManager.getInstance();
        if (!mqttManager.isConnected()) {
            mqttManager.connect(User.getUserName(), User.getPassword(), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Logger.i("mqtt 连接成功");
                    initSub();
                    pushLogin();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Logger.i("mqtt连接失败--" + exception);
                }
            });
        }
    }

    public static void initSub() {
        String[] topics = {"OrganizationAid/.added", "OrganizationInformation/.added", "user/" + User.getUserName() + "/.login"};
        for (String topic : topics) {
//            String cacheTopic = ACache.get(App.context, "topics").getAsString(topic);
//            if (TextUtils.isEmpty(cacheTopic)) {
            MQTTManager.getInstance().subscribe(topic, 2);
//            }
        }
    }

    private static void pushLogin() {
        if (!PreUtils.getBoolean("hasPushLogin", false)) {
            Logger.i("发送login消息");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = format.format(new Date());
            String msg = "{\"verb\":\"login\",\"from\":\"" + DeviceUtils.getUniqueIdForThisApp(App.context) + "\",\"time\":\"" + time + "\"}";
            MQTTManager.getInstance().publish("user/" + User.getUserName() + "/.login", 0, msg, false);
            PreUtils.putBoolean("hasPushLogin", true);
        } else {
            Logger.i("不发送login消息");
        }
    }


    public static void clear() {
        MQTTManager.release();
        ACache.get().clear();
        PreUtils.clear();
    }


//    on

}

