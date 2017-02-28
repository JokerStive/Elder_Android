package lilun.com.pension.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import lilun.com.pension.app.Event;
import me.yokeyword.fragmentation.SupportActivity;
import rx.Subscription;

/**
 * Created by yk on 2017/1/5.
 * 基类activity
 */
public abstract class BaseActivity<T extends IPresenter> extends SupportActivity {

    protected T mPresenter;
    private Subscription subscribe;
//    final FragmentController mFragments = FragmentController.createController(new HostCallbacks());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        EventBus.getDefault().register(this);

        getTransferData();

        initData();

        ButterKnife.bind(this);

        initPresenter();

        initView();

        initEvent();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void tokenFailure(Event.TokenFailure event) {
//        startActivity(new Intent(this, LoginActivity.class));
    }

    @Subscribe
    public void permissionDenied(Event.PermissionDenied event) {
//        Logger.d("prepare http me");
//        subscribe = NetHelper.getApi().getMe().
//                compose(RxUtils.handleResult())
//                .compose(RxUtils.applySchedule())
//                .subscribe(new RxSubscriber<Object>() {
//                    @Override
//                    public void _next(Object o) {
//
//                    }
//                });

    }


    @Override
    protected void onStart() {

        super.onStart();

    }

    //    ===============子类需要实现的方法=============================

    //获取传递过来的数据,可选,最先被调用
    protected void getTransferData() {

    }

    protected void initData() {
    }

    protected abstract int getLayoutId();

    //非Mvp就不用管
    protected abstract void initPresenter();

    //初始化布局
    protected abstract void initView();

    //初始化事件,listener...
    protected void initEvent() {
    }

    ;

    @Override
    public void onBackPressedSupport() {
        if (getSupportFragmentManager().getBackStackEntryCount() >= 1) {
            pop();
        } else {
            finish();
        }
    }

//    public void showHintToPop(){
//        FragmentTransaction ft = fragmentManager.beginTransaction().show(showFragment);
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if (mPresenter != null) {
            mPresenter.unBindView();
            mPresenter = null;
        }

        if (subscribe != null && subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }

        EventBus.getDefault().unregister(this);
    }
}
