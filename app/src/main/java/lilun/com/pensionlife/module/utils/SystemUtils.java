package lilun.com.pensionlife.module.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.List;

import lilun.com.pensionlife.BuildConfig;
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

    public static void installApk(Context context, File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // 第二个参数，即第一步中配置的authorities
            Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }
}