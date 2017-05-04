package lilun.com.pension.module.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.util.List;

import lilun.com.pension.app.App;

/**
 * Created by yk on 2017/1/4.
 * 关于系统的工具类
 */
public class SystemUtils {
    public static boolean checkHasNet() {// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        try {
            ConnectivityManager connectivity = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            ToastHelper.get().showWareShort("网络检查失败");
        }
        return false;
    }

    public static ActivityManager.RunningTaskInfo getTopTask() {
        ActivityManager mActivityManager = (ActivityManager) App.context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = mActivityManager.getRunningTasks(1);
        if (tasks != null && !tasks.isEmpty()) {
            return tasks.get(0);
        }
        return null;
    }

    public static boolean isTopActivity(String activityName) {
        ActivityManager.RunningTaskInfo topTask = getTopTask();
        if (topTask != null) {
            ComponentName topActivity = topTask.topActivity;
            String packageName = topActivity.getPackageName();
            String className = topActivity.getClassName();
            String packageNa = App.context.getPackageName();
            Logger.d("topActivity包名--" + packageName + "获取的包名--" + packageNa + "====" + "类名--" + className);
            if (TextUtils.equals(packageName, packageNa) && TextUtils.equals(className, activityName)) {
                return true;
            }
        }
        return false;
    }

}
