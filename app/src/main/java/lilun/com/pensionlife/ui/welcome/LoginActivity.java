package lilun.com.pensionlife.ui.welcome;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseActivity;
import lilun.com.pensionlife.module.bean.Account;
import lilun.com.pensionlife.ui.home.HomeActivity;
import lilun.com.pensionlife.ui.register.RegisterActivity;

/**
 * 登录V
 *
 * @author yk
 *         create at 2017/1/23 13:56
 *         email : yk_developer@163.com
 */

public class LoginActivity extends BaseActivity<LoginContract.Presenter> implements LoginContract.View {

    Account account;


    @Bind(R.id.tv_forget_password)
    TextView tvForgetPassword;
    @Bind(R.id.et_mobile)
    EditText etMobile;

    @Bind(R.id.tv_show_password)
    TextView tvShowPassword;

    @Bind(R.id.et_password)
    EditText etPassword;

    @Bind(R.id.bt_login)
    Button btLogin;

    @Bind(R.id.tv_new_account)
    TextView tvNewAccount;

    private boolean autologin;
    private boolean isShow = false;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            autologin = data.getBooleanExtra("autologin", false);
            account = (Account) data.getSerializableExtra("account");
            if (autologin) {
                etMobile.setText(account.getMobile());
                etPassword.setText(account.getPassword());
                login();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
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
        btLogin.setOnClickListener(v -> login());
        tvNewAccount.setOnClickListener(v -> {
            startRegister();
        });
        tvShowPassword.setOnClickListener(v -> {
            showPassword();
        });


        tvForgetPassword.setOnClickListener(v -> {
            startActivity(new Intent(this, ChangePasswordActivity.class));
        });
        if (autologin) {
            etMobile.setText(account.getMobile());
            etPassword.setText(account.getPassword());
            login();
        }
        etPassword.setOnKeyListener(editOnKeyListener);
    }


    @Override
    public void editViewEnterButton() {
        login();
    }

    private void startRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, 1);
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
