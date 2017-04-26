package lilun.com.pension.ui.welcome;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.base.BaseActivity;
import lilun.com.pension.module.bean.Account;
import lilun.com.pension.ui.home.HomeActivity;

/**
 * 登录V
 *
 * @author yk
 *         create at 2017/1/23 13:56
 *         email : yk_developer@163.com
 */

public class LoginActivity extends BaseActivity<LoginContract.Presenter> implements LoginContract.View {

    Account account;
    @Bind(R.id.et_mobile)
    EditText etMobile;

    @Bind(R.id.tv_show_password)
    TextView tvShowPassword;

    @Bind(R.id.et_password)
    EditText etPassword;

    @Bind(R.id.fab_next)
    FloatingActionButton fabNext;
    private boolean autologin;
    private boolean isShow = false;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    protected void initData() {
        autologin = getIntent().getBooleanExtra("autologin", false);
        account = (Account) getIntent().getSerializableExtra("account");
    }

    @Override
    protected void initPresenter() {
        mPresenter = new LoginPresenter(this);
        mPresenter.bindView(this);
    }

    @Override
    protected void initView() {
        fabNext.setOnClickListener(v -> login());
        tvShowPassword.setOnClickListener(v -> {
            showPassword();
        });
        if (autologin) {
            etMobile.setText(account.getMobile());
            etPassword.setText(account.getPassword());
            login();
        }
    }

    private void showPassword() {
        isShow = !isShow;
        Drawable drawable = null;
        if (isShow) {
            etPassword.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
            drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.password_show);
        } else {
            etPassword.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
            drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.password_not_show);
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvShowPassword.setCompoundDrawables(drawable, null, null, null);
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
