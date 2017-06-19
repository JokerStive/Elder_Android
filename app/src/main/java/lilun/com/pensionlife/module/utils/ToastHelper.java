package lilun.com.pensionlife.module.utils;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;


/**
 * Created by youke on 2016/5/26.
 * 提示框
 */
public class ToastHelper {
    private static ToastHelper instance;

    private final Handler handler = new Handler();
    private final Context context;
    private Toast toast;
    private WindowManager.LayoutParams mParams;

    class CancelRunnable implements Runnable {
        final Toast toast;

        CancelRunnable(Toast toast) {
            this.toast = toast;
        }

        @Override
        public void run() {
            toast.cancel();
        }
    }

    public static ToastHelper get(Context context) {
        if (instance == null) {
            synchronized (ToastHelper.class) {
                instance = new ToastHelper(context);
            }
        }
        return instance;
    }



    public static ToastHelper get() {
        if (instance == null) {
            synchronized (ToastHelper.class) {
                instance = new ToastHelper(App.context);
            }
        }
        return instance;
    }

    public void showShort(int resId) {
        showNormal(context.getText(resId), Toast.LENGTH_SHORT);
    }

    public void showShort(CharSequence text) {
        showNormal(text, Toast.LENGTH_SHORT);
    }

    public void showLong(int resId) {
        showNormal(context.getText(resId), Toast.LENGTH_LONG);
    }

    public void showLong(CharSequence text) {
        showNormal(text, Toast.LENGTH_LONG);
    }

    public void showNormal(int resId, int duration) {
        showNormal(context.getText(resId), duration);
    }

    public void showNormal(CharSequence text, int duration) {
        if (toast != null) {
            handler.post(new CancelRunnable(toast));
        }
        toast = new Toast(context);
        View layout = LayoutInflater.from(context).inflate(R.layout.bg_toast, null);
        toast.setView(layout);
        TextView textView = (TextView) layout.findViewById(R.id.toast_text);
//        textView.setTextColor(App.app.getResources().getBackgroundRes(R.color.default_white));
        textView.setText(text);
        toast.show();
    }

    public void showWare(CharSequence text, int duration) {
        if (toast != null) {
            handler.post(new CancelRunnable(toast));
        }
        toast = new Toast(context);
        View layout = LayoutInflater.from(context).inflate(R.layout.bg_toast, null);
        toast.setView(layout);
        TextView textView = (TextView) layout.findViewById(R.id.toast_text);
        textView.setBackgroundResource(R.drawable.bg_toast_red);
        textView.setText(text);
        toast.show();
    }

    private ToastHelper(Context context) {
        this.context = context;
    }

    public void showWareShort(CharSequence text) {
        showWare(text, Toast.LENGTH_SHORT);
    }
}
