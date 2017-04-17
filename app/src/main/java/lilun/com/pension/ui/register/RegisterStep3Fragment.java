package lilun.com.pension.ui.register;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
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
 * 验证码 验证
 * Created by zp on 2017/4/13.
 */

public class RegisterStep3Fragment extends BaseFragment<RegisterContract.PresenterStep3>
        implements RegisterContract.ViewStep3 {

    RegisterStep4Fragment fragmentStep4 = new RegisterStep4Fragment();
    Account account;
    String IDCode;
    @Bind(R.id.ll_input)
    LinearLayout llRegisterName;
    @Bind(R.id.tv_input_title)
    TextView tvInputTitle;
    @Bind(R.id.acet_input)
    AppCompatEditText acetRegisterCode;

    @OnClick(R.id.fab_go_next)
    public void onClick() {
        IDCode = acetRegisterCode.getText().toString().trim();
        if (checkRegisterCode(IDCode)) {
            mPresenter.checkIDCode(account.getMobile(), IDCode);
        }
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        account = (Account) arguments.getSerializable("account");
    }

    @Override
    protected void initPresenter() {
        mPresenter = new RegisterStep3Presenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register_step1;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        tvInputTitle.setText(R.string.register_code);
        acetRegisterCode.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
    }


    private boolean checkRegisterCode(String IDCode) {
        if (IDCode.length() != 6) {
            ToastHelper.get(getContext()).showWareShort("请输入6位验证码");
            return false;
        }
        return true;
    }

    private void goStep4() {
        ((RegisterActivity) _mActivity).setTitle();
        Bundle bundle = new Bundle();
        bundle.putSerializable("account", account);
        bundle.putString("IDCode", IDCode);
        fragmentStep4.setArguments(bundle);
        _mActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentStep4)
                .setCustomAnimations(R.anim.pop_container_in, R.anim.pop_container_out)
                .addToBackStack("")
                .commit();
    }

    @Override
    public void successOfCheckIDCode() {
        goStep4();
    }
}
