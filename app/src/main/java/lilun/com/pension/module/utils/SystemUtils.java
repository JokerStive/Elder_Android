package lilun.com.pension.module.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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
}
