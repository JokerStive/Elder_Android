package lilun.com.pension.app;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.baidu.mapapi.SDKInitializer;
import com.orhanobut.logger.Logger;

import org.litepal.LitePal;

import lilun.com.pension.module.utils.ACache;
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
        Logger.init(Config.TAG_LOGGER).methodCount(1).hideThreadInfo();

        //内存泄漏
//        LeakCanary.install(this);


//        数据库
        LitePal.initialize(this);

        SQLiteDatabase db = LitePal.getDatabase();


//        SDKInitializer.
        SDKInitializer.initialize(this);

    }


    /**
     * mqtt连接订阅
     */
    public static void mqttConnectAndSub() {
        String[] topics = new String[]{"OrganizationAid/.added"};
        int[] ops = new int[]{2};
        MQTTManager.getInstance().createConnect(User.getUserName(), User.getPassword(), topics, ops);

    }


    public static void clear() {
        MQTTManager.release();
        ACache.get().clear();
        PreUtils.clear();
    }

}

