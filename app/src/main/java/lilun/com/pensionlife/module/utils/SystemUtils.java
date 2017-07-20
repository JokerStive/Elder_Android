package lilun.com.pensionlife.module.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.List;

import lilun.com.pensionlife.app.App;

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


    public static boolean isExtendStorageEnable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 判断服务是否运行
     *
     * @param context
     * @param className
     * @return
     */
    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = am.getRunningServices(100);
        if (!(runningServices.size() > 0)) return isRunning;
        for (int i = 0; i < runningServices.size(); i++) {
            if (runningServices.get(i).service.getClassName().equals(className)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }


    public static String getVersionName(Context context) {
        String versionName = "";
        String pkName = context.getPackageName();
        try {
            versionName = context.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static void installApk(Context context, File file) {
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}