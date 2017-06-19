package lilun.com.pensionlife.ui.register;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.bean.Account;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.module.utils.ToastHelper;

/**
 * 手机号码获取验证码
 * Created by zp on 2017/4/13.
 */

public class RegisterStep2Fragment extends BaseFragment<RegisterContract.PresenterStep2>
        implements RegisterContract.ViewStep2 {

    Account account;
    @Bind(R.id.ll_input)
    LinearLayout llRegisterName;
    @Bind(R.id.tv_input_title)
    TextView tvInputTitle;
    @Bind(R.id.acet_input)
    AppCompatEditText acetRegisterPhone;


    @OnClick(R.id.fab_go_next)
    public void onClick() {
        String phone = acetRegisterPhone.getText().toString().trim();
        if (!StringUtils.isMobileNumber(phone)) {
            ToastHelper.get(getContext()).showWareShort("手机号码格式错误");
            return;
        }
        account.setMobile(phone);
        account.setUsername(phone);
        // goStep3();
        mPresenter.getIDCode(_mActivity, phone,"1");
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        account = (Account) arguments.getSerializable("account");
    }

    @Override
    protected void initPresenter() {
        mPresenter = new RegisterStep2Presenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register_step1;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        tvInputTitle.setText(getString(R.string.phone_number));
        acetRegisterPhone.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        acetRegisterPhone.setOnKeyListener(editOnKeyListener);
    }

    @Override
    public void editViewEnterButton() {
        onClick();
    }

    private void goStep3() {
        ((RegisterActivity) _mActivity).setTitle(account.getMobile());
        ((RegisterActivity) _mActivity).account = account;
        Bundle bundle = new Bundle();
        bundle.putSerializable("account", account);
        RegisterStep3Fragment fragmentStep3 = new RegisterStep3Fragment();
        fragmentStep3.setArguments(bundle);
        _mActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentStep3)
                .setCustomAnimations(R.anim.pop_container_in, R.anim.pop_container_out)
                .addToBackStack("")
                .commit();
    }


    @Override
    public void successOfIDCode() {
        goStep3();
    }
}
