package lilun.com.pension.app;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;

import org.litepal.LitePal;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import lilun.com.pension.module.utils.RxUtils;
import rx.Observable;
import rx.Subscriber;
//import cn.jpush.im.android.api.JMessageClient;

/**
*入口
*@author yk
*create at 2017/2/4 13:40
*email : yk_developer@163.com
*/
public class App extends Application {
    public static Context context;
    public Set<String> tags;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        tags =new HashSet<>();
        init();

    }

    private void init() {

        //日志
        Logger.init(Config.TAG_LOGGER).methodCount(1).hideThreadInfo();

        //内存泄漏
        LeakCanary.install(this);

        //激光推送
        JPushInterface.init(this);

        //数据库
        LitePal.initialize(this);

        SQLiteDatabase db = LitePal.getDatabase();

        //设置别名和标签
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                tags.add("地球村_中国");
                tags.add("重庆");
                tags.add("重庆市");
                tags.add("A小区");
                JPushInterface.setTags(context, tags, new TagAliasCallback() {
                    @Override
                    public void gotResult(int i, String s, Set<String> set) {
                        Logger.d("Jpush tage res code = "+i);
                        Logger.d("Jpush tags = "+set);
                    }
                });

            }
        })
                .compose(RxUtils.applySchedule())
                .subscribe();

    }

}

