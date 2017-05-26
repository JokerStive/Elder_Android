package lilun.com.pension.ui.welcome;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pension.R;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.bean.TokenInfo;
import lilun.com.pension.module.utils.PreUtils;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.module.utils.ToastHelper;
import lilun.com.pension.net.ApiException;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;
import lilun.com.pension.ui.register.RegisterActivity;
import lilun.com.pension.ui.register.RegisterStep2Presenter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * 忘记密码 验证码 验证
 * Created by zp on 2017/4/13.
 */

public class ChangePasswordFragment2 extends BaseFragment {

    private final int REGET_TIME = 60;
    CompositeSubscription cntDownRx;

    private String phone;
    String IDCode="";
    String token="";
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.title)
    TextView tvTitle;
    @Bind(R.id.title_desp)
    TextView tvTitleDesp;
    @Bind(R.id.ll_input)
    LinearLayout llRegisterName;
    @Bind(R.id.tv_input_title)
    TextView tvInputTitle;
    @Bind(R.id.actv_show_count_down)
    AppCompatTextView actvShowCntDown;
    @Bind(R.id.acet_input)
    AppCompatEditText acetRegisterCode;
    @Bind(R.id.fab_go_next)
    FloatingActionButton fabGoNext;


    @OnClick({R.id.fab_go_next, R.id.actv_show_count_down})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_go_next:
                IDCode = acetRegisterCode.getText().toString().trim();
                if (checkRegisterCode(IDCode)) {
                    postIdentifyUser(phone, IDCode);
                }
                break;
            case R.id.actv_show_count_down:
                if (actvShowCntDown.getText().toString().trim().equals(getString(R.string.reget))) {
                    getIDCode(phone);
                    return;
                }

                break;
        }
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        phone = arguments.getString("phone");
    }

    @Override
    protected void initPresenter() {
        mPresenter = new RegisterStep2Presenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_forget_password_step1;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        ivBack.setOnClickListener(v->{pop();});
        tvTitle.setText(R.string.input_IDcode);
        tvTitleDesp.setText("我们向 " + phone + " 发送了一个验证码，请输入该验证码");
        tvInputTitle.setText(R.string.register_code);
        acetRegisterCode.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        acetRegisterCode.setOnKeyListener(editOnKeyListener);
        actvShowCntDown.setVisibility(View.VISIBLE);
        startCnt();
    }

    @Override
    public void editViewEnterButton() {
        onClick(fabGoNext);
    }

    private boolean checkRegisterCode(String IDCode) {
        if (IDCode.length() != 6) {
            ToastHelper.get(getContext()).showWareShort("请输入6位验证码");
            return false;
        }
        return true;
    }

    private void goStep3() {
        Bundle bundle = new Bundle();
        ChangePasswordFragment3 fragment3 = new ChangePasswordFragment3();
        bundle.putString(User.token, token);
        fragment3.setArguments(bundle);
        start(fragment3);
    }


    public void successOfIDCode() {
        startCnt();
    }

    /**
     * 获取验证码请求
     * @param phone
     */
    public void getIDCode(String phone) {
        subscription.add(NetHelper.getApi()
                .getIDCode(phone, "2")
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>(_mActivity) {
                    @Override
                    public void _next(Object s) {
                        successOfIDCode();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (((ApiException) e).getErrorMessage().contains("Remote service error, code 15;")) {
                            super.onError();
                            ToastHelper.get().showWareShort("短信发送太多，请1小时后尝试");
                            return;
                        }
                        int[] errorCode = {600, 601, 604};
                        String[] errorMessage = {
                                "1分钟只能发送一条短信",
                                "短信发送太多，请1小时后尝试",
                                "该手机号码还未注册，请确认输入正确"};
                        super.onError(e, errorCode, errorMessage);
                    }
                }));
    }

    /**
     * 检测验证码及身份验证
     * @param phone
     * @param code
     */
    public void postIdentifyUser(String phone, String code) {
        subscription.add(NetHelper.getApi()
                .getIdentifyUser(phone, code)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<TokenInfo>(_mActivity) {
                    @Override
                    public void _next(TokenInfo retoken) {
                        if (retoken != null)
                            token = retoken.getId();
                        goStep3();
                    }
                }));
    }

    public void startCnt() {
        if (cntDownRx == null)
            cntDownRx = new CompositeSubscription();
        cntDownRx.add(Observable.timer(0, 1, TimeUnit.SECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return REGET_TIME - aLong;
                    }
                })
                .take(REGET_TIME + 1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {
                    Log.d("zp", res + "");

                    if (res == 0) {
                        stop();

                        actvShowCntDown.setText(getString(R.string.reget));
                        actvShowCntDown.setBackgroundResource(R.drawable.shape_rect_red_corner_5);

                    } else {
                        actvShowCntDown.setText(getString(R.string.count_down_seconds, res + ""));
                        actvShowCntDown.setBackgroundResource(R.drawable.shape_rect_gray_corner_5);
                    }
                }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop();
    }

    public void stop() {
        if (cntDownRx != null && !cntDownRx.isUnsubscribed()) {
            cntDownRx.unsubscribe();
            cntDownRx = null;
        }
    }
}
