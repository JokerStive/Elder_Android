package lilun.com.pension.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;
import lilun.com.pension.R;
import lilun.com.pension.app.Event;
import lilun.com.pension.module.adapter.PushInfoAdapter;
import lilun.com.pension.module.callback.MyCallBack;
import lilun.com.pension.widget.CardConfig;
import lilun.com.pension.widget.OverLayCardLayoutManager;
import me.yokeyword.fragmentation.SupportActivity;
import rx.Subscription;

/**
 * Created by yk on 2017/1/5.
 * 基类activity
 */
public abstract class BaseActivity<T extends IPresenter> extends SupportActivity {

    protected T mPresenter;
    private Subscription subscribe;
    private RecyclerView rvPushInfo;
    private FrameLayout mFrameLayout;
    private PushInfoAdapter pushInfoAdapter;
    private List<String> data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base);
        rvPushInfo = (RecyclerView) findViewById(R.id.rv_push_container);

        mFrameLayout = (FrameLayout) findViewById(R.id.fl_root_container);
        mFrameLayout.addView(LayoutInflater.from(this).inflate(getLayoutId(), null));


//        data = new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            data.add(i + "");
//        }

//        initPushBar();

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

    private void initPushBar() {
        if (data == null || data.size() == 0) {
            return;
        }

        pushInfoAdapter = new PushInfoAdapter(rvPushInfo, data, R.layout.item_push_info);


        rvPushInfo.setLayoutManager(new OverLayCardLayoutManager());
//        rvPushInfo.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false));
        rvPushInfo.setAdapter(pushInfoAdapter);
        CardConfig.initConfig(this);
        CardConfig.MAX_SHOW_COUNT = 3;


        ItemTouchHelper.Callback callback = new MyCallBack(rvPushInfo, pushInfoAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rvPushInfo);


        pushInfoAdapter.setOnPushClickListener(new PushInfoAdapter.onPushClickListener() {
            @Override
            public void onDeleteClick(String item) {
//                pushInfoAdapter.re

            }

            @Override
            public void onItemClick() {

            }

            @Override
            public void onExpandClick() {

            }
        });
    }

    @Override
    public void onBackPressedSupport() {
        if (getSupportFragmentManager().getBackStackEntryCount() >= 1) {
            pop();
        } else {
            finish();
        }
    }


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
