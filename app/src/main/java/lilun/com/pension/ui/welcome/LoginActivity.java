package lilun.com.pension.ui.welcome;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.base.BaseActivity;
import lilun.com.pension.ui.home.HomeActivity;

/**
 * 登录V
 *
 * @author yk
 *         create at 2017/1/23 13:56
 *         email : yk_developer@163.com
 */

public class LoginActivity extends BaseActivity<LoginContract.Presenter> implements LoginContract.View {


    @Bind(R.id.et_mobile)
    EditText etMobile;

    @Bind(R.id.tv_show_password)
    TextView tvShowPassword;

    @Bind(R.id.et_password)
    EditText etPassword;

    @Bind(R.id.fab_next)
    FloatingActionButton fabNext;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new LoginPresenter(this);
        mPresenter.bindView(this);
    }

    @Override
    protected void initView() {
        fabNext.setOnClickListener(v -> login());

    }

    private void login() {
        String phone = etMobile.getText().toString();
        String password = etPassword.getText().toString();
        mPresenter.login(phone, password);
    }


    @Override
    public void loginSuccess() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }


}
