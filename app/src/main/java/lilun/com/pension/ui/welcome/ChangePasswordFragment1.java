package lilun.com.pension.ui.welcome;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.module.utils.ToastHelper;
import lilun.com.pension.net.ApiException;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;
import lilun.com.pension.ui.register.RegisterContract;
import lilun.com.pension.ui.register.RegisterStep2Presenter;

/**
 * 忘记密码
 * Created by zp on 2017/5/25.
 */

public class ChangePasswordFragment1 extends BaseFragment<RegisterContract.PresenterStep2>
        implements RegisterContract.ViewStep2 {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.acet_input)
    AppCompatEditText acetInput;
    @Bind(R.id.tv_input_title)
    TextView tvInputTitle;
    @Bind(R.id.fab_go_next)
    FloatingActionButton fabGoNext;
    String phone = "";

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
        ivBack.setOnClickListener(v -> _mActivity.finish());
        fabGoNext.setOnClickListener(v -> {
            goStep2();
        });
        tvInputTitle.setText(getString(R.string.phone_number));
        acetInput.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        acetInput.setOnKeyListener(editOnKeyListener);
    }

    @Override
    public void editViewEnterButton() {
        goStep2();
    }

    private void goStep2() {
        phone = acetInput.getText().toString().trim();
        if (!StringUtils.isMobileNumber(phone)) {
            ToastHelper.get(getContext()).showWareShort("手机号码格式错误");
            return;
        }
        getIDCode(phone);
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
                        super.hideDialog();
                        if (e == null) {
                            ToastHelper.get(App.context).showWareShort("网络连接失败");
                            return;
                        }
                        if (((ApiException) e).getErrorMessage().contains("Remote service error, code 15;")) {
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

    public void successOfIDCode() {
        ChangePasswordFragment2 fragment2 = new ChangePasswordFragment2();
        Bundle bundle = new Bundle();
        bundle.putString("phone", phone);
        fragment2.setArguments(bundle);
        start(fragment2);

    }
}
