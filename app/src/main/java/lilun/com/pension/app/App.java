package lilun.com.pension.app;

import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;

import cn.jpush.android.api.JPushInterface;
//import cn.jpush.im.android.api.JMessageClient;

/**
*入口
*@author yk
*create at 2017/2/4 13:40
*email : yk_developer@163.com
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
        //日志，内存泄漏，极光推送
        Logger.init(Config.TAG_LOGGER).methodCount(1).hideThreadInfo();
        LeakCanary.install(this);
        JPushInterface.init(this);

//        JMessageClient.setDebugMode(true);
//        JMessageClient.init(this);

    }

}

