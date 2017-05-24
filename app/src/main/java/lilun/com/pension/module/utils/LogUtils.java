package lilun.com.pension.module.utils;

import com.orhanobut.logger.Logger;

import lilun.com.pension.BuildConfig;
import lilun.com.pension.app.Config;
import lilun.com.pension.app.ConfigUri;

/**
*
*@author yk
*create at 2017/5/4 11:52
*email : yk_developer@163.com
*/
public class LogUtils  {
    public static void d(String msg){
        if (BuildConfig.LOG_DEBUG){
            Logger.d(msg);
        }
    }

    public static void d(String tag, String msg){
        if (BuildConfig.LOG_DEBUG){
            Logger.d(tag,msg);
        }
    }


    public static void i(String msg){
        if (BuildConfig.LOG_DEBUG){
            Logger.i(msg);
        }
    }

    public static void i(String tag, String msg){
        if (BuildConfig.LOG_DEBUG){
            Logger.i(tag,msg);
        }
    }
}
