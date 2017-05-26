package lilun.com.pension.ui.welcome;

import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseActivity;
import lilun.com.pension.ui.home.HomeActivity;
import lilun.com.pension.ui.register.RegisterActivity;

/**
 * 欢迎页面
 *
 * @author yk
 *         create at 2017/2/15 8:48
 *         email : yk_developer@163.com
 */
public class WelcomeActivity extends BaseActivity implements View.OnClickListener {
    public static BaseActivity welcomeActivity = null;

    @Bind(R.id.btn_login)
    TextView btnLogin;
    @Bind(R.id.btn_register)
    TextView btnRegister;

    @Override
    protected void getTransferData() {
        Logger.d(User.getUserId());
        if (!TextUtils.isEmpty(User.getUserId())) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }


    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initView() {
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        welcomeActivity = this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                startActivity(new Intent(this, LoginActivity.class));
                //  finish();
                break;
            case R.id.btn_register:
                startActivity(new Intent(this, RegisterActivity.class));
                // finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return super.onKeyDown(keyCode, event);
    }
}
