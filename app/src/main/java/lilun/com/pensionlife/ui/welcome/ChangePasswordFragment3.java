package lilun.com.pensionlife.ui.welcome;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;

/**
 * 忘记密码3
 * Created by zp on 2017/4/13.
 */

public class ChangePasswordFragment3 extends BaseFragment {

    String token;
    String registerPassword;
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
    @Bind(R.id.acet_input)
    AppCompatEditText acetRegisterPassword;


    @OnClick(R.id.fab_go_next)
    public void onClick() {
        registerPassword = acetRegisterPassword.getText().toString().trim();
        if (checkRegisterPassword(registerPassword)) {
            changePassword(registerPassword, token);
        }
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        token = arguments.getString(User.token);
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_forget_password_step1;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        ivBack.setOnClickListener(v -> pop());
        tvTitle.setText(R.string.new_password);
        tvTitleDesp.setText(R.string.new_password_str);
        tvInputTitle.setText(R.string.password);
        acetRegisterPassword.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD | EditorInfo.TYPE_CLASS_TEXT);
        acetRegisterPassword.setOnKeyListener(editOnKeyListener);
    }

    @Override
    public void editViewEnterButton() {
        onClick();
    }

    private boolean checkRegisterPassword(String registerPassword) {
        if (TextUtils.isEmpty(registerPassword)) {
            ToastHelper.get(getContext()).showWareShort("请输入密码");
            return false;
        }
        if (registerPassword.length() < 6) {
            ToastHelper.get(getContext()).showWareShort("密码长度不足6位");
            return false;
        }
        return true;
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
                .subscribe(new RxSubscriber<Object>(_mActivity) {
                    @Override
                    public void _next(Object o) {
                        ToastHelper.get(_mActivity).showShort("修改密码成功");
                        _mActivity.finish();
                    }
                }));
    }

}
