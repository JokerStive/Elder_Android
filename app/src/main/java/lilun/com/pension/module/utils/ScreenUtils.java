package lilun.com.pension.module.utils;

import android.content.Context;
import android.util.DisplayMetrics;
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

    /**
     * 获得屏幕高度的分辨率
     *
     * @param context
     * @return
     */
    public static int getScreenWidthPixels(Context context)
    {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕高度的分辨率
     *
     * @param context
     * @return
     */
    public static int getScreenHeightPixels(Context context)
    {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }



    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context)
    {

        int statusHeight = -1;
        try
        {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return statusHeight;
    }

}
