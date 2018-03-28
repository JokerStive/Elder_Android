package lilun.com.pensionlife.ui.welcome;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.Bind;
import lilun.com.pensionlife.BuildConfig;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.Constants;
import lilun.com.pensionlife.base.BaseActivity;
import lilun.com.pensionlife.module.bean.Account;
import lilun.com.pensionlife.module.bean.AppVersion;
import lilun.com.pensionlife.module.utils.VersionCheck;
import lilun.com.pensionlife.ui.home.HomeActivity;
import lilun.com.pensionlife.ui.home.VersionDialogFragment;
import lilun.com.pensionlife.ui.home.info_setting.InfoSettingFilter;
import lilun.com.pensionlife.ui.register.ActivateAccountManager;
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

    @Bind(R.id.iv_logo)
    ImageView ivLogo;
    @Bind(R.id.ll_container)
    LinearLayout llContainer;
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
    @Bind(R.id.nsv_scrollview)
    NestedScrollView nsvView;
    @Bind(R.id.rl_container)
    RelativeLayout rlContainer;

    private boolean autologin;
    private boolean isShow = false;
    private int keyHeight; //屏幕1/3

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
        //弹起高度为屏幕高度的1/3
        keyHeight = this.getResources().getDisplayMetrics().heightPixels / 3; //获取屏幕高度

    }

    @Override
    protected void initPresenter() {
        mPresenter = new LoginPresenter(this);
        mPresenter.bindView(this);
        mPresenter.getVersionInfo(Constants.appName, Constants.version_latest);
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
        nsvView.addOnLayoutChangeListener(((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
             /* old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值
              现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起*/
            if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
                Log.e("wenzhihao", "up------>" + (oldBottom - bottom) + " " + rlContainer.getHeight());
                int dist = llContainer.getBottom() - bottom - rlContainer.getHeight();
                if (dist > 0) {
                    ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(llContainer, "translationY", 0.0f, -dist);
                    mAnimatorTranslateY.setDuration(300);
                    mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
                    mAnimatorTranslateY.start();
                    zoomIn(ivLogo, 0.6f, dist);
                }


            } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
                Log.e("wenzhihao", "down------>" + (bottom - oldBottom));
                if ((llContainer.getBottom() - oldBottom) > 0) {
                    ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(llContainer, "translationY", llContainer.getTranslationY(), 0);
                    mAnimatorTranslateY.setDuration(300);
                    mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
                    mAnimatorTranslateY.start();
                    //键盘收回后，logo恢复原来大小，位置同样回到初始位置
                    zoomOut(ivLogo, 0.6f);
                }

            }
        }));
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
        InfoSettingFilter.initInfoFilter();
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }


    /**
     * 显示版本升级信息
     *
     * @param version
     */
    @Override
    public void showVersionInfo(AppVersion version) {
        if (version == null) return;
        if (VersionCheck.compareVersion(BuildConfig.VERSION_NAME, version.getVersion())) {
            VersionDialogFragment.newInstance(version).show(this.getFragmentManager(), VersionDialogFragment.class.getSimpleName());
        }
    }

    @Override
    public void activateAccount(String mobile) {
        ActivateAccountManager.newInstance().activate(this, mobile);
    }

    /**
     * 缩小
     *
     * @param view
     */
    public static void zoomIn(final View view, float scale, float dist) {
        view.setPivotY(view.getHeight());
        view.setPivotX(view.getWidth() / 2);
        AnimatorSet mAnimatorSet = new AnimatorSet();
        ObjectAnimator mAnimatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, scale);
        ObjectAnimator mAnimatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, scale);
        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(view, "translationY", 0.0f, -dist);

        mAnimatorSet.play(mAnimatorTranslateY).with(mAnimatorScaleX);
        mAnimatorSet.play(mAnimatorScaleX).with(mAnimatorScaleY);
        mAnimatorSet.setDuration(300);
        mAnimatorSet.start();
    }

    /**
     * f放大
     *
     * @param view
     */
    public static void zoomOut(final View view, float scale) {
        view.setPivotY(view.getHeight());
        view.setPivotX(view.getWidth() / 2);
        AnimatorSet mAnimatorSet = new AnimatorSet();

        ObjectAnimator mAnimatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", scale, 1.0f);
        ObjectAnimator mAnimatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", scale, 1.0f);
        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(view, "translationY", view.getTranslationY(), 0);

        mAnimatorSet.play(mAnimatorTranslateY).with(mAnimatorScaleX);
        mAnimatorSet.play(mAnimatorScaleX).with(mAnimatorScaleY);
        mAnimatorSet.setDuration(300);
        mAnimatorSet.start();
    }
}



