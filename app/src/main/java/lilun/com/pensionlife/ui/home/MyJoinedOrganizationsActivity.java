package lilun.com.pensionlife.ui.home;

import android.view.KeyEvent;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseActivity;

/**
 * 我加入的组织
 */
public class MyJoinedOrganizationsActivity extends BaseActivity {

    private MyJoinedOrganizationsFragment fragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    protected void initView() {
        fragment = new MyJoinedOrganizationsFragment();
        replaceLoadRootFragment(R.id.fl_root_container, fragment, false);

    }

    @Override
    protected void initEvent() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && getTopFragment() instanceof MyJoinedOrganizationsFragment) {
            fragment.performBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
