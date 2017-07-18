package lilun.com.pensionlife.ui.welcome;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.base.BaseActivity;
import lilun.com.pensionlife.module.bean.TokenInfo;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.net.ApiException;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;
import lilun.com.pensionlife.widget.NormalTitleBar;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

public class ChangePasswordActivity extends BaseActivity {
    private final int REGET_TIME = 60;
    CompositeSubscription cntDownRx;
    String mobile = "";  //手机号
    String token = "";
    String code = "";  //验证码
    String newPassrod = "";  //新密码

    @Bind(R.id.title_bar)
    NormalTitleBar titleBar;
    @Bind(R.id.et_mobile)
    EditText etMobile;
    @Bind(R.id.et_verification_code)
    EditText etVerifCode;
    @Bind(R.id.tv_verif_code)
    TextView tvVerifCode;

    @Bind(R.id.et_password_first)
    EditText etPasswordFirst;
    @Bind(R.id.et_password_second)
    EditText etPasswordSecond;
    @Bind(R.id.bt_confirm_password)
    Button btConfirmPassrod;

    @Override
    protected int getLayoutId() {
        return R.layout.change_passowrd;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initView() {
        titleBar.setOnBackClickListener(() -> {
            finish();
        });
        titleBar.setTitle(getString(R.string.forget_password));
        etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                mobile = s.toString().trim();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        tvVerifCode.setText(getString(R.string.quest_verif_code));
        tvVerifCode.setOnClickListener(v -> {
            requestVerifCode();
        });
        btConfirmPassrod.setOnClickListener(v -> {
            requestChangePassword();
        });
    }

    // 软键盘的Enter事件响应
    @Override
    public void editViewEnterButton() {
        requestChangePassword();
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void onDestroy() {
        stop();
        super.onDestroy();
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
                        tvVerifCode.setClickable(true);
                        tvVerifCode.setText(getString(R.string.reget));

                    } else {
                        tvVerifCode.setClickable(false);
                        tvVerifCode.setText(getString(R.string.count_down_seconds, res + ""));
                    }
                }));
    }

    public void stop() {
        if (cntDownRx != null && !cntDownRx.isUnsubscribed()) {
            cntDownRx.unsubscribe();
            cntDownRx = null;
        }
    }

    private void requestVerifCode() {
        if (!StringUtils.isMobileNumber(mobile)) {
            ToastHelper.get().showShort("请输入正确的手机号码");
            return;
        }
        getIDCode(mobile);
    }


    private void requestChangePassword() {
        String password1 = etPasswordFirst.getText().toString().trim();
        String password2 = etPasswordSecond.getText().toString().trim();
        code = etVerifCode.getText().toString().trim();

        if (!StringUtils.isMobileNumber(mobile)) {
            ToastHelper.get().showShort("请输入正确的手机号码");
            return;
        }

        if (!StringUtils.isVerifCode(code)) {
            ToastHelper.get().showShort("请输入正确的验证码");
            return;
        }
        if (TextUtils.isEmpty(password1) || TextUtils.isEmpty(password2)) {
            ToastHelper.get().showShort(getString(R.string.new_password_str));
            return;
        }

        if (!password1.equals(password2)) {
            ToastHelper.get().showShort(getString(R.string.password_not_equal));
            return;
        }
        newPassrod = password1;
        postIdentifyUser(mobile, code);
    }

    /**
     * 获取验证码请求
     *
     * @param phone
     */
    public void getIDCode(String phone) {
        subscription.add(NetHelper.getApi()
                .getIDCode(phone, "2")
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>(this) {
                    @Override
                    public void _next(Object s) {
                        startCnt();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.hideDialog();
                        if (e == null) {
                            ToastHelper.get(App.context).showWareShort("网络连接失败");
                            return;
                        }
                        if (e instanceof ApiException) {
                            if (((ApiException) e).getErrorMessage().contains("Remote service error, code 15;")) {
                                ToastHelper.get().showWareShort("短信发送太多，请1小时后尝试");
                                return;
                            }
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
     *
     * @param phone
     * @param code
     */
    public void postIdentifyUser(String phone, String code) {
        subscription.add(NetHelper.getApi()
                .getIdentifyUser(phone, code)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<TokenInfo>(this) {
                    @Override
                    public void _next(TokenInfo retoken) {
                        if (retoken != null) {
                            token = retoken.getId();
                            changePassword(newPassrod, token);
                        }
                    }
                }));
    }

    /**
     * 修改密码请求
     *
     * @param password
     */
    public void changePassword(String password, String token) {
        subscription.add(NetHelper.getApi()
                .changePassword(password, token)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>(this) {
                    @Override
                    public void _next(Object o) {
                        ToastHelper.get().showShort("修改密码成功");
                        finish();
                    }
                }));
    }


}
