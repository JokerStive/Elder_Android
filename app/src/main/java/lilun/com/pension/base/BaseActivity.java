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
import lilun.com.pension.app.App;
import lilun.com.pension.app.Constants;
import lilun.com.pension.app.Event;
import lilun.com.pension.app.User;
import lilun.com.pension.module.adapter.PushInfoAdapter;
import lilun.com.pension.module.bean.Information;
import lilun.com.pension.module.bean.OrganizationAid;
import lilun.com.pension.module.bean.PushMessage;
import lilun.com.pension.module.callback.MyCallBack;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.module.utils.SystemUtils;
import lilun.com.pension.module.utils.ToastHelper;
import lilun.com.pension.module.utils.mqtt.MQTTManager;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;
import lilun.com.pension.ui.lbs.AnnounceInfoActivity;
import lilun.com.pension.ui.lbs.UrgentAidInfoActivity;
import lilun.com.pension.ui.welcome.LoginActivity;
import lilun.com.pension.ui.welcome.WelcomeActivity;
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
    private int pushAidInfoCunt = 0;
    private int pushInfoCunt = 0;


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


    /**
     * 登陆事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshPushMessage(Event.OffLine offLine) {
        startActivity(new Intent(this, WelcomeActivity.class));
        ToastHelper.get().showShort("您的账号已在其他设备登陆");
        MQTTManager.getInstance().unSubscribe("user/" + User.getUserName() + "/.login", null, null);
        App.clear();
        finish();
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
        String model = pushMessage.getModel();
        if (model.equals(Constants.organizationAid)) {
            OrganizationAid aid = gson.fromJson(pushMessage.getData(), OrganizationAid.class);
            if (aid.getKind() == 2) {
                if (pushAidInfoCunt == 0 && !SystemUtils.isTopActivity(UrgentAidInfoActivity.class.getName())) {
                    pushAidInfoCunt++;
                    Intent intent = new Intent(this, UrgentAidInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("aid", aid);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 123);
                }
                EventBus.getDefault().post(new Event.RefreshUrgentInfo());
            }
        } else if (model.equals(Constants.organizationInfo)) {
            Information Information = gson.fromJson(pushMessage.getData(), Information.class);
            if (pushInfoCunt == 0 && !SystemUtils.isTopActivity(Information.class.getName())) {
                pushInfoCunt++;
                Intent intent = new Intent(this, AnnounceInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("organizationInfo", Information);
                intent.putExtras(bundle);
                startActivityForResult(intent, 123);
            }
            EventBus.getDefault().post(new Event.RefreshUrgentInfo());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == 0) {
            pushAidInfoCunt = 0;
            pushInfoCunt = 0;
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


    public void showDialog() {
        if (dialog == null) {
            dialog = new RxProgressDialog(this);
        }

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


}
