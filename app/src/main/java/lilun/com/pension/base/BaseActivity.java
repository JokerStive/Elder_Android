package lilun.com.pension.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.ButterKnife;
import lilun.com.pension.R;
import lilun.com.pension.app.Event;
import lilun.com.pension.module.adapter.PushInfoAdapter;
import lilun.com.pension.module.bean.OrganizationAid;
import lilun.com.pension.module.bean.PushMessage;
import lilun.com.pension.module.callback.MyCallBack;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.module.utils.SystemUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;
import lilun.com.pension.ui.lbs.UrgentInfoActivity;
import lilun.com.pension.ui.welcome.LoginActivity;
import lilun.com.pension.widget.CardConfig;
import lilun.com.pension.widget.OverLayCardLayoutManager;
import lilun.com.pension.widget.progress.RxProgressDialog;
import me.yokeyword.fragmentation.SupportActivity;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

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
    private MyCallBack callback;
    protected CompositeSubscription subscription = new CompositeSubscription();
    private RxProgressDialog dialog;
    private int pushInfoCunt=0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base);
        rvPushInfo = (RecyclerView) findViewById(R.id.rv_push_container);

        mFrameLayout = (FrameLayout) findViewById(R.id.fl_root_container);
        mFrameLayout.addView(LayoutInflater.from(this).inflate(getLayoutId(), null));


        getTransferData();

        initData();

        ButterKnife.bind(this);

        initPresenter();

        initView();


        initEvent();

        EventBus.getDefault().register(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void tokenFailure(Event.TokenFailure event) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Subscribe
    public void permissionDenied(Event.PermissionDenied event) {
//        Logger.d("prepare http me");
        subscribe = NetHelper.getApi().getMe().
                compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>() {
                    @Override
                    public void _next(Object o) {

                    }
                });

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshPushMessage(Event.RefreshPushMessage event) {
        showPushMessage(getPushMessageFromDatabase());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshPushMessage(PushMessage pushMessage) {
        showExpressHelpPop(pushMessage);
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

    /**
     * 显示推送过来的消息
     */
    private void showPushMessage(List<PushMessage> pushMessages) {
        if (pushMessages == null || pushMessages.size() == 0) {
            rvPushInfo.setVisibility(View.GONE);
            return;
        }

        rvPushInfo.setVisibility(View.VISIBLE);
        if (pushInfoAdapter == null) {
            pushInfoAdapter = new PushInfoAdapter(rvPushInfo, pushMessages);
            rvPushInfo.setAdapter(pushInfoAdapter);
        } else {
            pushInfoAdapter.replaceAll(pushMessages);
        }

    }

    /**
     * 初始化推送消息栏
     */
    private void initRecyclerView() {
        rvPushInfo.setLayoutManager(new OverLayCardLayoutManager());
        CardConfig.initConfig(this);
        CardConfig.MAX_SHOW_COUNT = 3;

        callback = new MyCallBack(rvPushInfo);
        callback.setOnItemSwipedListener(() -> {
            if (pushInfoAdapter != null && pushInfoAdapter.getItemCount() != 0) {
                PushMessage item = pushInfoAdapter.getItem(pushInfoAdapter.getItemCount() - 1);
                pushInfoAdapter.remove(item);
                if (pushInfoAdapter.getItemCount() == 0) {
                    Logger.d("推送栏设置gone");
                    rvPushInfo.setVisibility(View.GONE);
                }

//                DataSupport.deleteAll(PushMessage.class, "king = ?", item.getKing());
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rvPushInfo);

        showPushMessage(getPushMessageFromDatabase());
    }


    /**
     * 从数据库中取消息
     */
    public List<PushMessage> getPushMessageFromDatabase() {
        List<PushMessage> allMessage = DataSupport.findAll(PushMessage.class);
        return allMessage;
    }

    /**
     * 显示紧急求助弹窗
     *
     * @param pushMessage
     */
    public void showExpressHelpPop(PushMessage pushMessage) {
        Gson gson = new Gson();
        OrganizationAid aid = gson.fromJson(pushMessage.getData(), OrganizationAid.class);
        if (aid.getKind() == 2) {
            if (pushInfoCunt==0 && !SystemUtils.isTopActivity(UrgentInfoActivity.class.getName())) {
                pushInfoCunt++;
                Intent intent = new Intent(this, UrgentInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("aid", aid);
                intent.putExtras(bundle);
                startActivityForResult(intent,123);
            }
            EventBus.getDefault().post(new Event.RefreshUrgentInfo(aid));
            List<PushMessage> allMessage = DataSupport.findAll(PushMessage.class);
            Logger.d("缓存的推送消息的数量---" + allMessage.size());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==123 && resultCode==0){
            pushInfoCunt=0;
        }
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


    protected void showDialog() {
        if (dialog == null) {
            dialog = new RxProgressDialog(this);
        }
        dialog.show();
    }

    protected void dismissDialog() {
        dialog.dismiss();
    }


}
