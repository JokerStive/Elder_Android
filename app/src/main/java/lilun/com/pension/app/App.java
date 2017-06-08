package lilun.com.pension.app;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.baidu.mapapi.SDKInitializer;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.Bugly;

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
import lilun.com.pension.module.utils.mqtt.MqttTopic;

/**
 * 入口
 *
 * @author yk
 *         create at 2017/2/4 13:40
 *         email : yk_developer@163.com
 */
public class App extends Application {
    public static Context context;
    public static int widthDP = 0;
    public static Date loginDate = null;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        init();

    }

    private void init() {

        //日志
        if (BuildConfig.LOG_DEBUG) {
//               Logger.init(Config.TAG_LOGGER).methodCount(1).hideThreadInfo();
            Logger.addLogAdapter(new AndroidLogAdapter());
        } else {
            //   Logger.init(Config.TAG_LOGGER).logLevel(LogLevel.NONE);
        }

        //内存泄漏
//        LeakCanary.install(this);


//        数据库
        LitePal.initialize(this);

        SQLiteDatabase db = LitePal.getDatabase();


//       百度地图
        SDKInitializer.initialize(this);

        getWidthDP();

        Bugly.init(getApplicationContext(), "b07eef1cd9", BuildConfig.DEBUG);
    }


    public static void resetMQTT() {
        Logger.i("尝试重链接mqtt");
        MQTTManager mqttManager = MQTTManager.getInstance();
        if (!mqttManager.isConnected()) {
            mqttManager.connect(User.getUserName(), User.getPassword(), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Logger.i("mqtt 连接成功");
                    loginDate = new Date();
                    initSub();
                    pushLogin();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Logger.i("mqtt连接失败--" + exception);
                }
            });
        } else {
            Logger.i("mqtt已经连接不需要再次连接");
        }
    }

    public static void initSub() {
        MqttTopic mqttTopic = new MqttTopic();
        String[] topics = {mqttTopic.normal_announce,
                mqttTopic.normal_announce,
                mqttTopic.login,
                mqttTopic.urgent_help};
        for (String topic : topics) {
            MQTTManager.getInstance().subscribe(topic, 2);
        }
    }

    private static void pushLogin() {
        if (!PreUtils.getBoolean("hasPushLogin", false)) {
            Logger.i("发送login消息");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = format.format(new Date());
            String msg = "{\"verb\":\"login\",\"from\":\"" + DeviceUtils.getUniqueIdForThisApp(App.context) + "\",\"time\":\"" + time + "\"}";
            MqttTopic mqttTopic = new MqttTopic();
            MQTTManager.getInstance().publish(mqttTopic.login, 0, msg, false);
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


    public void getWidthDP() {
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics pm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(pm);
        widthDP = (int) (pm.widthPixels / pm.density);
    }


}

