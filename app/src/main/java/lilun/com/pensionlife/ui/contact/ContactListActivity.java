package lilun.com.pensionlife.ui.contact;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseActivity;

public class ContactListActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    protected void initView() {
        replaceLoadRootFragment(R.id.fl_root_container, new ContactListFragment(), false);

    }

    @Override
    protected void initEvent() {

    }
}
