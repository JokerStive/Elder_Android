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
    private boolean needProgressBar;
    private RxProgressDialog dialog;

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
                if (dialog == null) {
                    dialog = new RxProgressDialog(activity);
                }
                dialog.show();
            } else {
                throw new RuntimeException("this is  not UI thread ");
            }
        }

    }

    @Override
    public void onCompleted() {
        if (needProgressBar) {
            dialog.cancel();
        }

    }

    @Override
    public void onError(Throwable e) {
        if (needProgressBar) {
            dialog.cancel();
        }

        if (e instanceof ApiException) {
            ToastHelper.get().showWareShort(((ApiException) e).getErrorMessage());
        } else {
            Logger.d(e.getMessage());
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
