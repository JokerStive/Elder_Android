package lilun.com.pension.module.utils;

import android.content.Context;
import android.view.WindowManager;

/**
*屏幕工具类
*@author yk
*create at 2017/2/6 10:45
*email : yk_developer@163.com
*/
public class ScreenUtils {

    public static  int  getScreenWith(Context context){
        WindowManager ss = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return ss.getDefaultDisplay().getWidth();
    }

    public static  int  getScreenHeight(Context context){
//        DisplayMetrics dm =context.getResources().getDisplayMetrics();
//        back dm.heightPixels;
        WindowManager ss = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return ss.getDefaultDisplay().getHeight();
    }

}
