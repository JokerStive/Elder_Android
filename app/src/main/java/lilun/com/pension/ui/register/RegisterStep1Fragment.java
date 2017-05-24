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

public class RegisterStep1Fragment extends BaseFragment {
    RegisterStep2Fragment fragmentStep2 = new RegisterStep2Fragment();
    Account account;
    @Bind(R.id.ll_input)
    LinearLayout llRegisterName;
    @Bind(R.id.tv_input_title)
    TextView tvInputTitle;
    @Bind(R.id.acet_input)
    AppCompatEditText acetRegisterName;


    @OnClick(R.id.fab_go_next)
    public void onClick() {
        if (checkRegisterName(acetRegisterName.getText().toString().trim()) == true) {
            account.setName((acetRegisterName.getText().toString().trim()));
            goStep2();
        }
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        account= (Account) arguments.getSerializable("account");
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
        tvInputTitle.setText(getString(R.string.nickname));
        acetRegisterName.setOnKeyListener(editOnKeyListener);
    }

    @Override
    public void editViewEnterButton() {
        if (checkRegisterName(acetRegisterName.getText().toString().trim()) == true) {
            account.setName((acetRegisterName.getText().toString().trim()));
            goStep2();
        }
    }


    public boolean checkRegisterName(String name) {
        if (TextUtils.isEmpty(name)) {
            ToastHelper.get(getContext()).showWareShort("请输入姓名");
            return false;
        }
        return true;
    }

    private void goStep2() {
        ((RegisterActivity) _mActivity).setTitle();
        Bundle bundle = new Bundle();
        bundle.putSerializable("account",account);
        ((RegisterActivity) _mActivity).account = account;
        fragmentStep2.setArguments(bundle);
        _mActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentStep2)
                .setCustomAnimations(R.anim.pop_container_in, R.anim.pop_container_out)
                .addToBackStack("")
                .commit();
    }
}
