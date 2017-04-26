package lilun.com.pension.ui.register;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pension.R;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.bean.Account;
import lilun.com.pension.module.utils.ToastHelper;

/**
 * Created by zp on 2017/4/13.
 */

public class RegisterStep4Fragment extends BaseFragment {

    Account account;
    String IDCode;
    String registerPassword;
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
            account.setPassword(registerPassword);
            goStep5();
        }
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        account = (Account) arguments.getSerializable("account");
        IDCode = arguments.getString("IDCode");
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register_step1;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        tvInputTitle.setText(R.string.password);
        acetRegisterPassword.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD | EditorInfo.TYPE_CLASS_TEXT);
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

    private void goStep5() {
        ((RegisterActivity) _mActivity).setTitle();
        Bundle bundle = new Bundle();
        bundle.putSerializable("account", account);
        bundle.putString("IDCode", IDCode);
        RegisterStep5Fragment fragmentStep5 = new RegisterStep5Fragment();
        fragmentStep5.setArguments(bundle);
        _mActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentStep5)
                .setCustomAnimations(R.anim.pop_container_in, R.anim.pop_container_out)
                .addToBackStack("")
                .commit();
    }

}
