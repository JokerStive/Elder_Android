package lilun.com.pension.net;

import android.app.Activity;

import com.orhanobut.logger.Logger;

import lilun.com.pension.app.App;
import lilun.com.pension.module.utils.ToastHelper;
import lilun.com.pension.widget.progress.RxProgressDialog;
import rx.Subscriber;

/**
 * Created by youke on 2017/1/3.
 * 异常统一处理
 */
public abstract class RxSubscriber<T> extends Subscriber<T> {

    private Activity activity;
    public boolean needProgressBar;
    public RxProgressDialog dialog;

    public RxSubscriber() {
    }

    public RxSubscriber(Activity content) {
        this.activity = content;
        needProgressBar = true;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (needProgressBar) {
            if (App.context.getMainLooper().isCurrentThread()) {
                if (activity != null) {
                    if (dialog == null) {
                        dialog = new RxProgressDialog(activity);
                    }
                    dialog.show();
                }
            } else {
                throw new RuntimeException("this is  not UI thread ");
            }
        }

    }

    @Override
    public void onCompleted() {
        if (needProgressBar) {
            if (activity != null) {
                dialog.cancel();
                dialog.dismiss();
            }
        }

    }

    @Override
    public void onError(Throwable e) {
        hideDialog();
        if (e == null) {
            ToastHelper.get(App.context).showWareShort("网络连接失败");
            return;
        }

        if (e instanceof ApiException) {
            ToastHelper.get().showWareShort(((ApiException) e).getErrorMessage());
        } else {
            Logger.d(e.getMessage());
            ToastHelper.get().showWareShort("connect timed out".equals(e.getMessage()) ? "连接超时" : e.getMessage());
        }
        _error();
    }

    public void hideDialog() {
        if (needProgressBar) {
            dialog.cancel();
        }
    }

    /**
     * 根据状态码更改显示的内容
     *
     * @param e
     * @param errorCode
     * @param errorMessage
     */
    public void onError(Throwable e, int[] errorCode, String[] errorMessage) {
        hideDialog();

        if (e == null) {
            ToastHelper.get().showWareShort("网络连接失败");
            return;
        }

        if (e instanceof ApiException) {
            for (int i = 0; i < errorCode.length; i++) {
                if (111 == ((ApiException) e).getErrorCode())
                    ToastHelper.get().showWareShort(((ApiException) e).getErrorMessage());
                if (errorCode[i] == ((ApiException) e).getErrorCode())
                    ToastHelper.get().showWareShort(errorMessage[i]);
            }
        } else {
            Logger.d(e.getMessage());
            ToastHelper.get().showWareShort("connect timed out".equals(e.getMessage()) ? "连接超时" : e.getMessage());
        }
        _error();
    }

    @Override
    public void onNext(T t) {
        _next(t);
    }

    public abstract void _next(T t);

    public void _error() {
    }
}
