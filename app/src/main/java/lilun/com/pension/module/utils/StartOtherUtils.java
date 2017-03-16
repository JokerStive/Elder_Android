package lilun.com.pension.module.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;

/**
 * 打开手机系统其他app
 * Created by zp on 2017/3/16.
 */

public class StartOtherUtils {

    public static void cellPhone(Activity context, String number) {
        if (!StringUtils.isMobileNo(number)) {
            ToastHelper.get(context).showWareShort("电话号码异常");
            return;
        }
        //用intent启动拨打电话
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            ToastHelper.get(context).showWareShort("调用失败");
        }
    }
}
