package lilun.com.pension.ui.register;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.base.BaseActivity;
import lilun.com.pension.module.bean.Account;

/**
 * Created by zp on 2017/4/13.
 */

public class RegisterActivity extends BaseActivity {
    ArrayList<String> titleList = new ArrayList<>();
    ArrayList<String> titleDespList = new ArrayList<>();
    Account account = new Account();
    RegisterStep1Fragment fragmentStep1 = new RegisterStep1Fragment();
    RegisterStep6Fragment fragmentStep6 = new RegisterStep6Fragment();

    @Bind(R.id.title)
    TextView tvTitle;
    @Bind(R.id.title_desp)
    TextView tvTitleDesp;

    @OnClick(R.id.iv_back)
    public void onclick(View view) {
        if (getSupportFragmentManager().getBackStackEntryCount() == 6) {

        } else if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            finish();
        } else {
            pop();
            setPopTitle();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new RegisterStep2Presenter();
        mPresenter.bindView(this);
    }

    @Override
    protected void initView() {
        titleList.addAll(Arrays.asList(App.context.getResources().getStringArray(R.array.register_title_list)));
        titleDespList.addAll(Arrays.asList(App.context.getResources().getStringArray(R.array.register_title_desp_list)));

        setTitle();
        Bundle bundle = new Bundle();
        bundle.putSerializable("account", account);
        fragmentStep1.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragmentStep1)
                .setCustomAnimations(R.anim.pop_container_in, R.anim.pop_container_out)
                .addToBackStack("")
                .commit();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 6) {
                return false;
            }
            if (getSupportFragmentManager().getBackStackEntryCount() == 1)
                finish();
            else {
                onBackPressed();
                setPopTitle();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setTitle() {
        int index = getSupportFragmentManager().getBackStackEntryCount();
        tvTitle.setText(titleList.get(index));
        tvTitleDesp.setText(titleDespList.get(index));
    }

    public void setPopTitle() {
        int index = getSupportFragmentManager().getBackStackEntryCount() - 1;
        tvTitle.setText(titleList.get(index));
        tvTitleDesp.setText(titleDespList.get(index));
    }

    public void setTitle(String phone) {
        int index = getSupportFragmentManager().getBackStackEntryCount();
        tvTitle.setText(titleList.get(index));
        tvTitleDesp.setText(titleDespList.get(index).replace("phone", phone));
    }


}
