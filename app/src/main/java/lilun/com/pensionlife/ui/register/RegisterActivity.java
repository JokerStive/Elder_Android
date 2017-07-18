package lilun.com.pensionlife.ui.register;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseActivity;

/**
 * Created by zp on 2017/4/13.
 */

public class RegisterActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_register1;
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    public void onBackPressedSupport() {
        if (getSupportFragmentManager().getBackStackEntryCount() <= 1)
            finish();
        else
            super.onBackPressedSupport();

    }

    @Override
    protected void initView() {
          loadRootFragment(R.id.fragment_container, new RegisterAccountFragment());
        // loadRootFragment(R.id.fragment_container, new RegisterAvatorFragment());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
