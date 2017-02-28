package lilun.com.pension.app;

import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.jpush.android.api.JPushInterface;
import lilun.com.pension.module.utils.GlideImageLoader;

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



        //TODO 测试用,换自定义图片selector
        ThemeConfig theme = new ThemeConfig.Builder()
        .build();

        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
//                .setEnableEdit(true)
//                .setEnableCrop(true)
//                .setEnableRotate(true)
//                .setCropSquare(true)
//                .setEnablePreview(true)
        .build();


        ImageLoader imageloader = new GlideImageLoader();
        CoreConfig coreConfig = new CoreConfig.Builder(context, imageloader, theme)
//                .setDebug(BuildConfig.DEBUG)
                .setFunctionConfig(functionConfig)
        .build();
        GalleryFinal.init(coreConfig);
    }

}

