package lilun.com.pensionlife.base;

import android.app.Activity;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by youke on 2016/12/29.
 * 基类Presenter,控制rxjava订阅的生命周期
 */
public class RxPresenter<T> implements IPresenter<T> {

    protected T view;
    private CompositeSubscription mCompositeSubscription;

    protected void unSubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    protected void addSubscribe(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void bindView(T view) {
        this.view = view;
    }

    @Override
    public void unBindView() {
        view = null;
        unSubscribe();
    }

    /**
     * 获取到当前View所在的Activity
     */
    public Activity getActivity() {
        if (view instanceof BaseFragment) {
            return ((BaseFragment) view).getActivity();
        } else if (view instanceof BaseActivity) {
            return (BaseActivity) view;
        }
        return null;
    }
}
