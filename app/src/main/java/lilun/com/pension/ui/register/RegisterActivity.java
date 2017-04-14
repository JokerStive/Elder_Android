package lilun.com.pension.ui.register;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.base.BaseActivity;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.module.utils.ToastHelper;

/**
 * Created by zp on 2017/4/13.
 */

public class RegisterActivity extends BaseActivity<RegisterContract.Presenter> implements RegisterContract.View {
    ArrayList<String> titleList = new ArrayList<>();
    ArrayList<String> titleDespList = new ArrayList<>();

    RegisterStep1Fragment fragmentStep1 = new RegisterStep1Fragment();
    RegisterStep2Fragment fragmentStep2 = new RegisterStep2Fragment();
    RegisterStep3Fragment fragmentStep3 = new RegisterStep3Fragment();
    RegisterStep4Fragment fragmentStep4 = new RegisterStep4Fragment();

    String registerName = "";
    String registerPhone = "";
    String code = "";


    private String registerPassword = "";

    @Bind(R.id.title)
    TextView tvTitle;
    @Bind(R.id.title_desp)
    TextView tvTitleDesp;
    @Bind(R.id.rl_go_next)
    RelativeLayout rlGoNext;

    @OnClick(R.id.iv_back)
    public void onclick(View view) {
        if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            finish();
        } else {
            pop();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new RegisterPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected void initView() {
        titleList.addAll(Arrays.asList(App.context.getResources().getStringArray(R.array.register_title_list)));
        titleDespList.addAll(Arrays.asList(App.context.getResources().getStringArray(R.array.register_title_desp_list)));

        setTitle();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragmentStep1)
                .addToBackStack("")
                .commit();
        rlGoNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goNext();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1)
                finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setTitle() {
        int index = getSupportFragmentManager().getBackStackEntryCount();
        tvTitle.setText(titleList.get(index));
        tvTitleDesp.setText(titleDespList.get(index));
    }

    public void getRegisterName() {
        registerName = fragmentStep1.getRegisterName();
    }

    public boolean checkRegisterName(String name) {
        if (TextUtils.isEmpty(name)) {
            ToastHelper.get(getApplicationContext()).showWareShort("请输入姓名");
            return false;
        }
        return true;
    }

    private boolean checkRegisterCode() {
        if (code.length() != 6) {
            ToastHelper.get(getApplicationContext()).showWareShort("验证码错误");
            return false;
        }
        return true;
    }

    private boolean checkRegisterPassword() {
        if (TextUtils.isEmpty(registerPassword)) {
            ToastHelper.get(getApplicationContext()).showWareShort("请输入密码");
            return false;
        }
        if (registerPassword.length() < 6) {
            ToastHelper.get(getApplicationContext()).showWareShort("密码长度不足6位");
            return false;
        }
        return true;
    }


    public void goNext() {
        int step = getSupportFragmentManager().getBackStackEntryCount();
        if (4 == step) {
            registerPassword = fragmentStep4.getRegisterPassword();
            if (checkRegisterPassword()) {
                goStep5();
            }
        } else if (3 == step) {
            code = fragmentStep3.getRegisterCode();
            if (checkRegisterCode()) goStep4();
        } else if (2 == step) {
            registerPhone = fragmentStep2.getRegisterPhone();
            if (!StringUtils.isMobileNumber(registerPhone)) {
                ToastHelper.get(getApplicationContext()).showWareShort("手机号码格式错误");
                return;
            }
            //mPresenter.getIDCode(registerPhone);
            successOfIDCode("400401");

        } else if (1 == step) {
            registerName = fragmentStep1.getRegisterName();
            if (checkRegisterName(registerName) == true) {
                goStep2();
            }
        }

    }


    private void goStep2() {
        setTitle();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentStep2)
                .addToBackStack("")
                .commit();
    }

    private void goStep3() {
        setTitle();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentStep3)
                .addToBackStack("")
                .commit();
    }

    private void goStep4() {
        setTitle();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentStep4)
                .addToBackStack("")
                .commit();
    }

    private void goStep5() {
        setTitle();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentStep4)
                .addToBackStack("")
                .commit();
    }

    @Override
    public void successOfIDCode(String code) {
        this.code = code;
        goStep3();
    }
}
